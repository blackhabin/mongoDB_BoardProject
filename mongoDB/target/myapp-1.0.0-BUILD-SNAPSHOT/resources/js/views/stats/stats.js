/**
 * stats.js
 */

	var drowPI = function (title, subtitle, innerSize, depth, data) {
	    $('#drowDiv').highcharts({
	        chart: {
	            type: 'pie',
	            options3d: {
	                enabled: true,
	                alpha: 45
	            }
	        },
	        title: {
	            text: title
	        },
	        subtitle: {
	            text: subtitle
	        },
	        plotOptions: {
	            pie: {
 	                innerSize: innerSize,
 	                depth: depth
	
	            }
	        },
            //Controller에 담은 데이터 불러오기 data: data
	        series: [{
	            name: '타겟 카운트',
	            data: data
	        }]
	    });
	};