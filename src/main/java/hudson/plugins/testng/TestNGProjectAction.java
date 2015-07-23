package hudson.plugins.testng;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Result;
import hudson.plugins.testng.results.ClassResult;
import hudson.plugins.testng.results.MethodResult;
import hudson.plugins.testng.results.TestNGTestResult;
import hudson.tasks.test.TestResultProjectAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Action to associate the TestNG reports with the project
 *
 * @author nullin
 */
public class TestNGProjectAction extends TestResultProjectAction implements ProminentProjectAction {

   private transient boolean escapeTestDescp;
   private transient boolean escapeExceptionMsg;
   private transient boolean showFailedBuilds;

   public TestNGProjectAction(AbstractProject<?, ?> project,
         boolean escapeTestDescp, boolean escapeExceptionMsg, boolean showFailedBuilds) {
      super(project);
      this.escapeExceptionMsg = escapeExceptionMsg;
      this.escapeTestDescp = escapeTestDescp;
      this.showFailedBuilds = showFailedBuilds;
   }

   protected Class<TestNGTestResultBuildAction> getBuildActionClass() {
      return TestNGTestResultBuildAction.class;
   }

   public boolean getEscapeTestDescp()
   {
      return escapeTestDescp;
   }

   public boolean getEscapeExceptionMsg()
   {
      return escapeExceptionMsg;
   }

   /**
    * Getter for property 'project'.
    *
    * @return Value for property 'project'.
    */
   public AbstractProject<?, ?> getProject() {
      return super.project;
   }

   /**
    * {@inheritDoc}
    */
   public String getIconFileName() {
      return PluginImpl.ICON_FILE_NAME;
   }

   /**
    * {@inheritDoc}
    */
   public String getDisplayName() {
      return PluginImpl.DISPLAY_NAME;
   }

   /**
    * Getter for property 'graphName'.
    *
    * @return Value for property 'graphName'.
    */
   public String getGraphName() {
      return PluginImpl.GRAPH_NAME;
   }

   /**
    * {@inheritDoc}
    */
   public String getUrlName() {
      return PluginImpl.URL;
   }

   /**
    * {@inheritDoc}
    */
   public String getSearchUrl() {
      return PluginImpl.URL;
   }

   public TestNGTestResultBuildAction getLastCompletedBuildAction() {
      for (AbstractBuild<?, ?> build = getProject().getLastCompletedBuild();
               build != null; build = build.getPreviousCompletedBuild()) {
         final TestNGTestResultBuildAction action = build.getAction(getBuildActionClass());
         if (action != null) {
            return action;
         }
      }
      return null;
   }

   /**
    * Returns <code>true</code> if there is a graph to plot.
    *
    * @return Value for property 'graphAvailable'.
    */
   public boolean isGraphActive() {
      AbstractBuild<?, ?> build = getProject().getLastBuild();
      // in order to have a graph, we must have at least two points.
      int numPoints = 0;
      while (numPoints < 2) {
         if (build == null) {
            return false;
         }
         if (build.getAction(getBuildActionClass()) != null) {
            numPoints++;
         }
         build = build.getPreviousBuild();
      }
      return true;
   }

   /**
    * Returns json for charting
    *
    * @return a json for a chart
    */
   public String getChartJson() {
      JSONObject jsonObject = new JSONObject();
      JSONArray passes = new JSONArray();
      JSONArray fails = new JSONArray();
      JSONArray skips = new JSONArray();
      JSONArray buildNum = new JSONArray();
      JSONArray durations = new JSONArray();

      int count = 0;

      Set<Integer> loadedBuilds = getProject()._getRuns().getLoadedBuilds().keySet(); // cf. AbstractTestResultAction.getPreviousResult(Class, false)
      for (AbstractBuild<?, ?> build = getProject().getLastBuild();
           build != null && count++ < 30; build = loadedBuilds.contains(build.number - 1) ? build.getPreviousCompletedBuild() : null) {
         TestNGTestResultBuildAction action = build.getAction(getBuildActionClass());

         if (build.getResult() == null || build.getResult().isWorseThan(Result.FAILURE)) {
            //We don't want to add aborted or builds with no results into the graph
            continue;
         }

         if (action != null) {
            passes.add(action.getTotalCount() - action.getFailCount() - action.getSkipCount());
            fails.add(action.getFailCount());
            skips.add(action.getSkipCount());
            buildNum.add(Integer.toString(build.getNumber()));
            durations.add(build.getDuration());
         }
      }
      jsonObject.put("pass", passes);
      jsonObject.put("fail", fails);
      jsonObject.put("skip", skips);
      jsonObject.put("buildNum", buildNum);
      jsonObject.put("duration", durations);
      return jsonObject.toString();
   }

   /**
    * Returns json for testing Visualizer
    *
    * @return a json for a testing visualizer
    */
   public String getVizJson() {
      JSONArray jsonObject = new JSONArray();
      List<JSONObject> threads = new ArrayList<JSONObject>();
      threads.add(makeThreadObject(1));

      List<Long> threadEndTimes = new ArrayList<Long>();
      threadEndTimes.add(Integer.toUnsignedLong(0));

      AbstractBuild<?, ?> build = getProject().getLastBuild();
      if (build.getResult() == null || build.getResult().isWorseThan(Result.FAILURE)) {
         //We don't want to add aborted or builds with no results into the graph
         return jsonObject.toString();
      }

      TestNGTestResultBuildAction action = build.getAction(getBuildActionClass());
      //Err, I think this is the only way?
      for (TestNGTestResult result : action.getResult().getTestList()) {
         for(ClassResult classResult : result.getClassList()) {
            for(MethodResult methodResult : classResult.getTestMethods()) {
               boolean newThread = true;

               int threadIndex = 0;
               for(Long endTime : threadEndTimes) {
                  if(methodResult.getStartTime() >= endTime) {
                     newThread = false;
                     threads.get(threadIndex).getJSONArray("times").add(makeTimeObject(methodResult));
                     threadEndTimes.set(threadIndex, methodResult.getEndTime());
                     break;
                  }
                  threadIndex++;
               }
               if(newThread){
                  threads.add(makeThreadObject(threads.size() + 1));
                  threadEndTimes.add(methodResult.getEndTime());
               }
            }
         }
      }

      for(JSONObject thread : threads) {
         jsonObject.add(thread);
      }

      return jsonObject.toString();
   }

   private JSONObject makeTimeObject(MethodResult methodResult) {
      JSONObject times = new JSONObject();
      times.put("starting_time", methodResult.getStartTime());
      times.put("ending_time", methodResult.getEndTime());
      times.put("status", methodResult.getStatus());
      times.put("name", methodResult.getName());
      return times;
   }

   private JSONObject makeThreadObject(int threadNum) {
      JSONObject thread = new JSONObject();
      JSONArray times = new JSONArray();
      thread.put("label", "Thread "+Integer.toString(threadNum));
      thread.put("times", times);
      return thread;
   }

   public String getUnsortedJson() {
      JSONArray jsonObject = new JSONArray();

      AbstractBuild<?, ?> build = getProject().getLastBuild();
      if (build.getResult() == null || build.getResult().isWorseThan(Result.FAILURE)) {
         //We don't want to add aborted or builds with no results into the graph
         return jsonObject.toString();
      }

      TestNGTestResultBuildAction action = build.getAction(getBuildActionClass());
      //Err, I think this is the only way?
      for (TestNGTestResult result : action.getResult().getTestList()) {
         for(ClassResult classResult : result.getClassList()) {
            for(MethodResult methodResult : classResult.getTestMethods()) {
               JSONObject method = new JSONObject();
               method.put("starting_time", methodResult.getStartTime());
               method.put("ending_time", methodResult.getEndTime());
               method.put("status", methodResult.getStatus());
               method.put("name", methodResult.getName());
               jsonObject.add(method);
            }
         }
      }
      return jsonObject.toString();
   }
}
