On Error Resume Next
Set pdf = CreateObject("AcroPDF.PDF")
If Err.Number <> 0 Then
    WScript.Echo "Failed: " & Hex(Err.Number)
Else
    WScript.Echo "Success: ActiveX loaded"
End If
