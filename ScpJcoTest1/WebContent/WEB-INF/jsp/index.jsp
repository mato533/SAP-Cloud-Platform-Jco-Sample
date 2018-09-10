<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
h2, h3 {
	text-align: center;
}

table, td, th {
	border: 1px black solid;
}

table {
	width: 70%;
	margin: 0 auto;
	border: solid 1px;
}
</style>
<title>RFC Connection Test</title>
</head>
<body>
	<h2>RFC Connection Test</h2>
	<h3>
		Logged in as <a href="logout"><%=request.getRemoteUser()%></a>
	</h3>
	<form action="execute" method="post">
		<table>
			<tr>
				<td>Input Text</td>
				<td><input type="text" name="requtext"
					style="width: 100%; box-sizing: border-box"></td>
			</tr>
			<tr>
				<td align="center" colspan="2" style="border-style: none"><input
					type="submit" style="width: 70%; box-sizing: border-box;"></td>
			</tr>
		</table>
	</form>
</body>
</html>