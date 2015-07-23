function testViz(id, data) {
    var sortedData = data.sort(function(a,b){
        if(a.ending_time > b.ending_time){
            return 1;
        } else if(a.ending_time < b.ending_time) {
            return -1;
        } else {
            return 0;
        }
    });

    var finalData = [{label: "Thread1", times:[]}];
    var threadEndTimes = [0];

    sortedData.forEach(function(test){
        var newThread = true;
        var threadIndex = 0;

        for(var i = 0; i < threadEndTimes.length; i++){
            var threadTime = threadEndTimes[i];
            if(test.starting_time >= threadTime) {
                newThread = false;
                finalData[threadIndex].times.push(test);
                threadEndTimes[threadIndex] = test.ending_time;
                break;
            }
            threadIndex++;
        }
        if(newThread){
            finalData.push({label: "Thread"+(finalData.length+1), times:[test]});
            threadEndTimes.push(test.ending_time);
        }
    });
    console.log(finalData);
    data = finalData;
      var width = data[0].times.length * 50;

        var chart = d3.timeline()
          .width(width)
          .stack()
          .margin({left:70, right:30, top:0, bottom:0})
          .hover(function (d, i, datum) {
          // d is the current rendering object
          // i is the index during d3 rendering
          // datum is the id object
            var div = $('#hoverRes');
            var colors = chart.colors();
            div.find('.coloredDiv').css('background-color', colors(i))
            div.find('#name').text(datum.label);
          })
          .click(function (d, i, datum) {
            alert(d.name);
          })
          .scroll(function (x, scale) {
            $("#scrolled_date").text(scale.invert(x) + " to " + scale.invert(x+width));
          });
        var svg = d3.select("#"+id).append("svg").attr("width", width)
          .datum(data).call(chart);

}
