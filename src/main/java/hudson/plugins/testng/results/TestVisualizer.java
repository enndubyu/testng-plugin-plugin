package hudson.plugins.testng.results;

import hudson.model.AbstractBuild;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle results related to a single test class
 */
@SuppressWarnings("serial")
public class TestVisualizer extends BaseResult {

    private String pkgName; //name of package containing this class

    //cache
    private Map<String, GroupedTestRun> testRunMap = null;

    public TestVisualizer(String pkgName, String name) {
        super(name);
        this.pkgName = pkgName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getCanonicalName() {
        if (PackageResult.NO_PKG_NAME.equals(pkgName)) {
            return getName();
        } else {
            return pkgName + "." + getName();
        }
    }

    @Exported(visibility = 999)
    @Override
    public String getName() {
      return "tv";
   }

    @Override
    public void setOwner(AbstractBuild<?, ?> owner) {
        super.setOwner(owner);
    }

    @Override
    public Object getDynamic(String token, StaplerRequest req, StaplerResponse rsp) {
        return null;
    }

    @Override
    public List<MethodResult> getChildren() {
        return null;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

}
