package com.sriyanksiddhartha.speechtotext;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	//Layout
	private ImageView image11;
	private ImageView image12;
	private ImageView image13;
	private ImageView image21;
	private ImageView image22;
	private ImageView image23;
	private ImageView image31;
	private ImageView image32;
	private ImageView image33;
	private ImageView restart;
	private TextView title;

	//Atributes
	private boolean user;
	private ArrayList<ArrayList<String>> listOLists;
	private String userNumber;
	private TextToSpeech textToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image11 = (ImageView) findViewById(R.id.img11);
		image12 = (ImageView) findViewById(R.id.img12);
		image13 = (ImageView) findViewById(R.id.img13);
		image21 = (ImageView) findViewById(R.id.img21);
		image22 = (ImageView) findViewById(R.id.img22);
		image23 = (ImageView) findViewById(R.id.img23);
		image31 = (ImageView) findViewById(R.id.img31);
		image32 = (ImageView) findViewById(R.id.img32);
		image33 = (ImageView) findViewById(R.id.img33);
		restart = (ImageView) findViewById(R.id.restart);

		title = (TextView) findViewById(R.id.title);

		this.user = true;

		textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int i) {
				if (i == TextToSpeech.SUCCESS) {
					int result = textToSpeech.setLanguage(Locale.ENGLISH);

					if (result == TextToSpeech.LANG_MISSING_DATA ||
							result == TextToSpeech.LANG_NOT_SUPPORTED) {

						Log.e("TTS", "Language not supported");
					} else {
						//
					}
				} else {
					Log.e("TTS", "Initialization failed");
				}
			}
		});

		restartArray();
	}

	@Override
	protected void onDestroy() {
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}

		super.onDestroy();
	}

	// Actions
	public void getSpeechInput(View view) {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, 10);
		} else {
			Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
		}
	}

	public void restart(View view) {
		this.restart.setVisibility(View.INVISIBLE);
		this.title.setVisibility(View.INVISIBLE);
		restartArray();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case 10:
				if (resultCode == RESULT_OK && data != null) {
					ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					String stringResult = result.get(0);

					ImageView imageView = null;
					String newValue = this.user ? "1" : "2";
					String currentValue = "";
					switch (stringResult) {
						case "11":
							currentValue = this.listOLists.get(0).get(0);
							if (currentValue.equals("0")) {
								imageView = this.image11;
								this.listOLists.get(0).set(0, newValue);
							}
							break;
						case "12":
							currentValue = this.listOLists.get(0).get(1);
							if (currentValue.equals("0")) {
								imageView = this.image12;
								this.listOLists.get(0).set(1, newValue);
							}
							break;
						case "13":
							currentValue = this.listOLists.get(0).get(2);
							if (currentValue.equals("0")) {
								imageView = this.image13;
								this.listOLists.get(0).set(2, newValue);
							}
							break;
						case "21":
							currentValue = this.listOLists.get(1).get(0);
							if (currentValue.equals("0")) {
								imageView = this.image21;
								this.listOLists.get(1).set(0, newValue);
							}
							break;
						case "22":
							currentValue = this.listOLists.get(1).get(1);
							if (currentValue.equals("0")) {
								imageView = this.image22;
								this.listOLists.get(1).set(1, newValue);
							}
							break;
						case "23":
							currentValue = this.listOLists.get(1).get(2);
							if (currentValue.equals("0")) {
								imageView = this.image23;
								this.listOLists.get(1).set(2, newValue);
							}
							break;
						case "31":
							currentValue = this.listOLists.get(2).get(0);
							if (currentValue.equals("0")) {
								imageView = this.image31;
								this.listOLists.get(2).set(0, newValue);
							}
							break;
						case "32":
							currentValue = this.listOLists.get(2).get(1);
							if (currentValue.equals("0")) {
								imageView = this.image32;
								this.listOLists.get(2).set(1, newValue);
							}
							break;
						case "33":
							currentValue = this.listOLists.get(2).get(2);
							if (currentValue.equals("0")) {
								imageView = this.image33;
								this.listOLists.get(2).set(2, newValue);
							}
							break;
						default:
							break;
					}

					if (imageView != null)  {
						imageView.setVisibility(View.VISIBLE);
						if(this.user) {
							imageView.setImageResource(R.mipmap.kim);
							this.user = !this.user;
						} else {
							imageView.setImageResource(R.mipmap.donald);
							this.user = !this.user;
						}
						this.title.setVisibility(View.INVISIBLE);
					} else {
						this.title.setText("Invalid, try again");
						textToSpeech.speak(this.title.getText().toString(),
								TextToSpeech.QUEUE_FLUSH, null, null);
						this.title.setVisibility(View.VISIBLE);
					}

					if (isWinner()) {
						this.title.setText(String.format("Player %s wins", this.userNumber));
						this.title.setVisibility(View.VISIBLE);
						textToSpeech.speak(this.title.getText().toString(),
								TextToSpeech.QUEUE_FLUSH, null, null);
						setFinish();
					}

					if(isDraw()) {
						this.title.setText("It's a tie");
						textToSpeech.speak(this.title.getText().toString(),
								TextToSpeech.QUEUE_FLUSH, null, null);
						this.title.setVisibility(View.VISIBLE);
						setFinish();
					}
				}
				break;
		}
	}

	private boolean isWinner() {
		boolean isWinner;

		//Check the rows
		String first = this.listOLists.get(0).get(0);
		String second = this.listOLists.get(0).get(1);
		String third = this.listOLists.get(0).get(2);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		first = this.listOLists.get(1).get(0);
		second = this.listOLists.get(1).get(1);
		third = this.listOLists.get(1).get(2);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		first = this.listOLists.get(2).get(0);
		second = this.listOLists.get(2).get(1);
		third = this.listOLists.get(2).get(2);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		//Check the columns
		first = this.listOLists.get(0).get(0);
		second = this.listOLists.get(1).get(0);
		third = this.listOLists.get(2).get(0);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		first = this.listOLists.get(0).get(1);
		second = this.listOLists.get(1).get(1);
		third = this.listOLists.get(2).get(1);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		first = this.listOLists.get(0).get(2);
		second = this.listOLists.get(1).get(2);
		third = this.listOLists.get(2).get(2);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		//Check the diagonal
		first = this.listOLists.get(0).get(0);
		second = this.listOLists.get(1).get(1);
		third = this.listOLists.get(2).get(2);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		first = this.listOLists.get(0).get(2);
		second = this.listOLists.get(1).get(1);
		third = this.listOLists.get(2).get(0);

		isWinner = this.threeStringsAreEqual(first, second, third);

		if (isWinner) {
			this.userNumber = first;
			return isWinner;
		}

		return isWinner;
	}

	private boolean threeStringsAreEqual(String first, String second, String third) {
		boolean areEquals;
		areEquals = (first.equals(second));
		return (second.equals(third) && areEquals && first != "0");

	}

	private boolean isDraw() {
		boolean isDraw = true;
		for (int i=0; i< this.listOLists.size(); i++) {
			for (int j=0;j <this.listOLists.get(0).size(); j++) {
				isDraw = (this.listOLists.get(i).get(j) != "0") && isDraw;
			}
		}
		return isDraw;
	}

	private void setFinish() {
		this.image11.setVisibility(View.INVISIBLE);
		this.image12.setVisibility(View.INVISIBLE);
		this.image13.setVisibility(View.INVISIBLE);
		this.image21.setVisibility(View.INVISIBLE);
		this.image22.setVisibility(View.INVISIBLE);
		this.image23.setVisibility(View.INVISIBLE);
		this.image31.setVisibility(View.INVISIBLE);
		this.image32.setVisibility(View.INVISIBLE);
		this.image33.setVisibility(View.INVISIBLE);
		this.restart.setVisibility(View.VISIBLE);
	}

	private void restartArray() {
        this.listOLists = new ArrayList<>();

        ArrayList<String> linea1 = new ArrayList<>();
        linea1.add("0");
        linea1.add("0");
        linea1.add("0");
        ArrayList<String> linea2 = new ArrayList<>();
        linea2.add("0");
        linea2.add("0");
        linea2.add("0");
        ArrayList<String> linea3 = new ArrayList<>();
        linea3.add("0");
        linea3.add("0");
        linea3.add("0");

        this.listOLists.add(linea1);
        this.listOLists.add(linea2);
        this.listOLists.add(linea3);
    }

}
