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
script(src:"${app.rootUrl}/plugin/testng-plugin/js/draw_viz.js")


l.layout(title: "Test Execution Timeline") {
    st.include(page: "sidepanel.jelly", it: my.project)
    l.main_panel() {
        h1("Test Execution Timeline")
        div(id: "viz")
    }
}

script() {
    text("\nvar vizData = ${my.getVizJson()};")
    text("\ntestViz('viz', vizData);")
}