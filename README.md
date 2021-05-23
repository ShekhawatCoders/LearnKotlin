# LearnKotlin

Overview :-

1. Create service ( Foreground service )
2. Create Broadcast receiver ( start foreground service )
3. Convert text to speech
4. Create media player app
5. Keep track of network state like phone is connected to network or not
6. Keep track of phone state like phone is in dialing or ideal mode 

<hr>
<b><i>Splash Activity :-</i></b>

<pre><i>// hold for 3 seconds
Handler().postDelayed({
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
},3000)</i></pre>

<hr>
<b><i>TextToSpeech :-</i></b>

<pre><i>tts = TextToSpeech(applicationContext) {
        if(it == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(1.0f)
            val voice = Voice("Alexa",Locale.ENGLISH,Voice.QUALITY_VERY_LOW,Voice.LATENCY_VERY_LOW,false,null)
            tts.voice = voice
        }
    }</i></pre>
        
<hr>
<b><i>Play Media on device Above28 :-</i></b>

<pre><i>val contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
// Toast.makeText(applicationContext,contentUri.toString(),Toast.LENGTH_SHORT).show()
val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.RELATIVE_PATH)
val cursor = contentResolver.query(contentUri,projection,null,null,null)
val from = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME)

val adapter = SimpleCursorAdapter(this,android.R.layout.simple_list_item_activated_1,cursor,from,
intArrayOf(android.R.id.text1,android.R.id.text2),0)

val listView = findViewById<ListView>(R.id.LV_Show_Media)
listView.adapter = adapter

listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> // this is another function
    val id = listView.getChildAt(position).findViewById<TextView>(android.R.id.text1).text
    val name = listView.getChildAt(position).findViewById<TextView>(android.R.id.text2).text
    if(player.isPlaying) {
        player.pause()
        player.release()
    }
    val externalUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id.toString().toLong())
    player = MediaPlayer.create(this,externalUri)
    player.isLooping = true
    player.start()
}</i></pre>      
        
        
<hr>
<b><i>Network Status :-</i></b>

<pre><i>val nm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
showNetworkState(nm)

private fun showNetworkState(nm: ConnectivityManager) {
    nm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            // Network Connected
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            // Network Dis-Connected
        }
    })
}
</i></pre>     

<hr>
<b><i>Phone Status :-</i></b>

<pre><i>val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
showPhoneState(tm)

@RequiresApi(Build.VERSION_CODES.Q)
private fun showPhoneState(tm: TelephonyManager) {
    if(!checkPermission()) return
    val data = tm.dataNetworkType
    val list = tm.emergencyNumberList
    // findViewById<TextView>(R.id.TV_Phone_State).text = "$data \n $list"
    tm.listen(object : PhoneStateListener() {
        override fun onDataConnectionStateChanged(state: Int) {
            super.onDataConnectionStateChanged(state)
            var data = ""
            when(state) {
                TelephonyManager.DATA_CONNECTED -> data = "Connected"
                TelephonyManager.DATA_DISCONNECTED -> data = "Disconnected"
            }
        }
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            var data = ""
            when(state) {
                TelephonyManager.CALL_STATE_RINGING -> data = "Call is in ringing mode"
                TelephonyManager.CALL_STATE_IDLE -> data = "Call is in ideal state mode"
            }
            tts.speak(data,TextToSpeech.QUEUE_FLUSH,null,"1001")
        }
    }, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or PhoneStateListener.LISTEN_CALL_STATE)
}</i></pre>
