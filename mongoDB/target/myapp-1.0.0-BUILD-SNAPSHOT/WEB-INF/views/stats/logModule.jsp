<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그 통계 처리</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Bootstrap 설정 -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- highcharts 설정 -->  
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/highcharts-3d.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	 // 항목 이름과 데이터가 함께 있는 데이터
	 var data = ${data};
	 //alert(data);
	 // donet chart drow
	 //drowPI(제목, 부제목,안에 비어 있는 원 반지름(0으로 설정하면 파이차트가 됨),3D파이의높이,표시할 데이터);
	 drowPI("통계","사용자가 자주 사용하는 메뉴 통계", 100, 65, data);
	});
	  
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
	</script>

</head>
	<body>
		<div class="container">
			<h1>로그 통계 처리</h1>
			<div  id="drowDiv" style="height:400px;"></div>
		</div>
	</body>
</html>