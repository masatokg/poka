package com.poka;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextToSpeech3  extends Activity implements View.OnClickListener,
TextToSpeech.OnInitListener {
	
SQLiteDatabase sdb = null;
MySQLiteHelper helper = null;


private TextToSpeech mTextToSpeech;
private EditText mEditText;
private static int MY_DATA_CHECK_CODE = 1;
@Override
protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);

// レイアウトを設定する
 setContentView(R.layout.texttospeech3);

 // 読み上げるメッセージの入力ボックスを取得する
 mEditText = (EditText) findViewById(R.id.editText);

 // ボタン押したら入力されたテキストを読み上げる
 Button button = (Button) findViewById(R.id.button);
 button.setOnClickListener(this);
}

@Override
protected void onResume() {
	// TODO 自動生成されたメソッド・スタブ
	super.onResume();

	Button btn1 = (Button)findViewById(R.id.btn1);
	btn1.setOnClickListener(this);


	Button btn2 = (Button)findViewById(R.id.btn2);
	btn2.setOnClickListener(this);


	if(sdb == null){
		helper = new MySQLiteHelper(getApplicationContext());
	}
	try{
		sdb = helper.getWritableDatabase();
	}catch(SQLiteException e){

		return;
	}

}

@Override
protected void onDestroy() {
 super.onDestroy();
// TextToSpeech を解放する
 if (mTextToSpeech != null) {
	 mTextToSpeech.shutdown();
   }    
 }

@Override
public void onClick(View v) {
 if (mTextToSpeech == null) {
	 // 初回はテキスト読み上げ可能かチェックする
	 Intent checkIntent = new Intent();
	 checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	 startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
 } else {
	// テキストを読み上げる
	 speech();       
	 }
	Intent intent = null;
	switch(v. getId()){
		case R.id.btn2:

			EditText etv = (EditText)findViewById(R.id.editText);
			String inputMsg = etv.getText().toString();
			Toast.makeText(TextToSpeech3.this,"さくせいしました",Toast.LENGTH_SHORT).show();


			if(inputMsg!=null && !inputMsg.isEmpty())
			{
				helper.insertSerifu(sdb,inputMsg);
			}

			etv.setText("");
			break;


		case R.id.btn1:
			intent = new Intent(TextToSpeech3.this,KousinActivity.class);
			startActivity(intent);
			break;
	}
 }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// テキスト読み上げ可能チェックから戻った場合
 if (requestCode == MY_DATA_CHECK_CODE) {
	 if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
		  // 音声リソースが見つかったので TextToSpeech を開始する (-> onInit)
		 mTextToSpeech = new TextToSpeech(this, this);
	 } else {
		// 音声リソースがなければダウンロードする
		 Intent installIntent = new Intent();
		 installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		 startActivity(installIntent);
	 	}       
	 }
 }
@Override
public void onInit(int status) {
 if (status == TextToSpeech.SUCCESS) {
	 // 日本語に対応していれば日本語に設定する （無くても良い）
	 Locale locale = Locale.JAPANESE;
	 if (mTextToSpeech.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
		 mTextToSpeech.setLanguage(locale);
	 } else {
		 Toast.makeText(this, "It does not support the Japanese.", Toast.LENGTH_SHORT).show();
	 }      
	 // テキストを読み上げる
	 speech();
 } else {
	 Toast.makeText(this, "TextToSpeech is not supported.", Toast.LENGTH_SHORT).show();
 }    
}
private void speech() {
 // テキスト読み上げ中であれば停止する
 if (mTextToSpeech.isSpeaking()) {
	 mTextToSpeech.stop();
 }
 // テキストを読み上げる
 String message = mEditText.getText().toString();
 mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
	}
}

