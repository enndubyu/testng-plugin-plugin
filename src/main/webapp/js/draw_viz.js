function testViz(id, data) {
    console.log(data);
    var head = document.getElementsByTagName('head')[0];

    var script = '<script type="text/javascript" src="https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1.1","packages":["viz"]}]}"></script>';
    var div = document.createElement('div');
    div.innerHTML = script;
    var element = div.childNodes[0];
    head.appendChild(element);

//    head.appendChild(document.createNode('<script type="text/javascript" src="https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1.1","packages":["viz"]}]}"></script>'));


        var container = document.getElementById('viz');
        var chart = new google.visualization.Timeline(container);
        var dataTable = new google.visualization.DataTable();

        dataTable.addColumn({ type: 'string', id: 'President' });
        dataTable.addColumn({ type: 'date', id: 'Start' });
        dataTable.addColumn({ type: 'date', id: 'End' });
        dataTable.addRows([
          [ 'Washington', new Date(1789, 3, 30), new Date(1797, 2, 4) ],
          [ 'Adams',      new Date(1797, 2, 4),  new Date(1801, 2, 4) ],
          [ 'Jefferson',  new Date(1801, 2, 4),  new Date(1809, 2, 4) ]]);

        chart.draw(dataTable);






//    var width = 600;
////    var testData = [
////        {label: "testA", times: [{"starting_time": 1355752800000, "ending_time": 1355759900000}, {"starting_time": 1355767900000, "ending_time": 1355774400000}]},
////        {label: "testB", times: [{"starting_time": 1355759910000, "ending_time": 1355761900000}, ]},
////        {label: "testC", times: [{"starting_time": 1355761910000, "ending_time": 1355763910000}]},
////        {label: "testD", times: [{"starting_time": 1355759800000, "ending_time": 1355761900000}, ]},
////        {label: "testE", times: [{"starting_time": 1355759910000, "ending_time": 1355761900000}, ]},
////        {label: "testF", times: [{"starting_time": 1355759970000, "ending_time": 1355761900000}, ]}
////    ];
//
//    var chart = d3.timeline()
//        .width(width)
//        .stack()
//        .margin({left:70, right:30, top:0, bottom:0})
//        .hover(function (d, i, datum) {
//        // d is the current rendering object
//        // i is the index during d3 rendering
//        // datum is the id object
////            var div = $('#hoverRes');
////            var colors = chart.colors();
////            div.find('.coloredDiv').css('background-color', colors(i))
////            div.find('#name').text(datum.label);
//        })
//        .click(function (d, i, datum) {
//            alert(datum.label);
//        })
//        .tickFormat({
////            format: d3.time.format("%I %p"),
////            tickTime: d3.time.minutes,
//            tickInterval: 5,
//            tickSize: (-10)
//        });
//    var svg = d3.select("#"+id).append("svg").attr("width", width).datum(data).call(chart);
//
////    //vertical lines
////    svg.selectAll(".vline").data(d3.range(26)).enter()
////        .append("line")
////        .attr("x1", function (d) {
////        return d * 20;
////    })
////        .attr("x2", function (d) {
////        return d * 20;
////    })
////        .attr("y1", function (d) {
////        return 0;
////    })
////        .attr("y2", function (d) {
////        return 500;
////    })
////        .style("stroke", "#eee");
////
////    // horizontal lines
////    svg.selectAll(".vline").data(d3.range(26)).enter()
////        .append("line")
////        .attr("y1", function (d) {
////        return d * 20;
////    })
////        .attr("y2", function (d) {
////        return d * 20;
////    })
////        .attr("x1", function (d) {
////        return 0;
////    })
////        .attr("x2", function (d) {
////        return 500;
////    })
////        .style("stroke", "#eee");
}

