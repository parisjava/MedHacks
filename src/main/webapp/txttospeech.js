<script type="text/javascript">
function voiceInputOver(val){
     alert("Voice input is complete");
     alert("Your input is " + val);
}
</script>
<input onwebkitspeechchange="voiceInputOver(this.value)" x-webkit-speech />