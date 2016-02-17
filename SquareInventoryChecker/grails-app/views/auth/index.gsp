<div id="authInfo">
	<table>
		<tr>
			<td>Application ID</td>
			<td>${application_id}</td>
		</tr>
		<tr>
			<td>oAuth Token:</td>
			<td>${token}</td>
		</tr>
		<tr>
			<td>Last renewed:</td>
			<td>${lastRenewed}</td>
		</tr>
	</table>
</div>

<br>

<g:link action="authorize">Authorize app</g:link>
<g:link action="renewToken">Renew token</g:link>