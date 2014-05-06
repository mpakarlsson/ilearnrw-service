$(function(){
	loadData('stats/test');
	$("button#1").click(function()
		{
			loadData('stats/test');
		});
	$("button#2").click(function()
			{
				loadData('stats/test2');
			});
	$("button#3").click(function()
			{
				loadData('stats/test3');
			});
	$("button#4").click(function()
			{
				loadData('stats/test4');
			});
	$("button#5").click(function()
			{
				loadData('stats/test5');
			});
});
function loadData(url)
{
	$.ajax({
		url: url,
		type : 'POST',
		data: JSON.stringify({'userId':5,'test':'hi there'}),
		contentType : 'application/json'
		}).done(function(data){
			var data_obj = data;
			var d1 = [];
			var tks = [];
			var labels = [];
			var hi = 0.5;

			for(var i in data_obj)
			{
				labels.push(i);
				tks.push([hi,i]);
			    d1.push([hi,data_obj[i]/1]);
			    hi = hi+1;
			}
			
			function testt(obj)
			{
				return labels[obj.index] + ":" + obj.y;
			}
			Flotr.draw(
					$("#main")[0],
					{
						data:d1
					}, 
					{
				        bars: {
				            show: true,
				            horizontal: false,
				            shadowSize: 0,
				            barWidth: 0.5
				        },
				        mouse: {
				            track: true,
				            trackFormatter: testt,
				            relative: true
				        },
				        xaxis: {
				        	ticks: tks,
				        },
				        yaxis: {
				            min: 0,
				            autoscaleMargin: 1
				        }
				    });
		});
};

