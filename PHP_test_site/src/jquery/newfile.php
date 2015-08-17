<html>
<head>
	<script type="text/javascript" src="jquery.js"></script>
	<script type="text/javascript">
		function get() {
			$.post('data.php', { name: form.name.value },
				function(output) {
					$('#age').html(output).show();
				})
		}
	</script>
</head>
<body>
	<form name="form">
		<input type="text" name="name"><input type="button" value="Get" onClick="get();">
	</form>
	<div id="age">
	</div>
</body>
</html>