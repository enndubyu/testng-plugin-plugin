package hudson.plugins.testng.TestNGProjectAction

import hudson.plugins.testng.TestNGProjectAction
import hudson.plugins.testng.util.TestResultHistoryUtil

f = namespace(lib.FormTagLib)
l = namespace(lib.LayoutTagLib)
t = namespace("/lib/hudson")
st = namespace("jelly:stapler")

link(rel: "stylesheet", href:"${app.rootUrl}/plugin/testng-plugin/css/c3.min.css")
link(rel: "stylesheet", href:"${app.rootUrl}/plugin/testng-plugin/css/d3-timeline.css")

script(src:"${app.rootUrl}/plugin/testng-plugin/js/d3.min.js")
script(src:"${app.rootUrl}/plugin/testng-plugin/js/d3-timeline.min.js")
script(src:"${app.rootUrl}/plugin/testng-plugin/js/c3.min.js")
script(src:"${app.rootUrl}/plugin/testng-plugin/js/draw_results.js")
script(src:"${app.rootUrl}/plugin/testng-plugin/js/draw_viz.js")


l.layout(title: "TestNG Results Trend") {
    st.include(page: "sidepanel.jelly", it: my.project)
    l.main_panel() {

        h1("TestNG Results Trends")
        if (my.isGraphActive()) {
            div(id: "chart")
            //img(lazymap: "graphMap?rel=../", alt: "[Test result trend chart]", src: "graph")
        } else {
            p("Need at least 2 builds with results to show trend graph")
        }

        br()
        def buildNumber = my.project.lastCompletedBuild.number
        h2() {
            text("Latest Test Results (")
            a(href: "${my.project.lastCompletedBuild.upUrl}${buildNumber}/${my.urlName}") {
                text("build #${buildNumber}")
            }
            text(")")
        }

        def lastCompletedBuildAction = my.lastCompletedBuildAction
        if (lastCompletedBuildAction) {
            p() {
                raw("${TestResultHistoryUtil.toSummary(lastCompletedBuildAction)}")
            }
        } else {
            p("No builds have successfully recorded TestNG results yet")
        }
        div(id: "viz")
    }
}

script() {
    text("\nvar data = ${my.getChartJson()};")
    text("\nvar vizData = ${my.getVizJson()};")
    text("\nvar unsortedData = ${my.getUnsortedJson()};")
    text("\nresultsGraph('chart', data);")
    text("\ntestViz('viz', vizData);")
}