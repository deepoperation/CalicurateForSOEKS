package jp.tank.deepoperation.CalicurateForSOEKS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CalicurateForSOEKSActivity extends Activity implements View.OnClickListener {
	//メニューアイテムID
	private final static int FP = LinearLayout.LayoutParams.FILL_PARENT;
	private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
	private SharedPreferences pref;
	TextView titleText;
	TextView IDText;
	TextView countText;
	TextView averageText;
	TextView centralText;
	TextView maxText;
	TextView minText;
	EditText mainParameter;
	EditText subParameter;
	EditText deployText;
	CheckBox coefficientCheck;
	EditText coefficientMajor;
	EditText coefficientMinor;
	ArrayList<Double> list;
	SoundPool soundPool;
	int soundOk;
	int soundError;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		pref = getSharedPreferences("CalicurateForSOEKS", MODE_PRIVATE);
		list = new ArrayList<Double>();
	    soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    soundOk = soundPool.load(this, R.raw.ok, 1);
	    soundError = soundPool.load(this, R.raw.error, 1);
	    
		// レイアウトの生成 (1)
	    ScrollView view = new ScrollView(this);
		setContentView(view);	    
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(Color.rgb(255, 255, 255));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LinearLayout.LayoutParams(FP, FP));
		view.addView(layout);
		// テキストビューの生成 (2)
		LinearLayout textLayout = new LinearLayout(this);
		textLayout.setBackgroundColor(Color.rgb(0, 0, 0));
		textLayout.setOrientation(LinearLayout.VERTICAL);
		titleText = new TextView(this);
		titleText.setText("μSv/h単位で入力してください");
		titleText.setTextColor(Color.rgb(255,255,255));
		textLayout.addView(titleText);
		layout.addView(textLayout);
		LinearLayout parameterLayout = new LinearLayout(this);
		parameterLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainParameter =new EditText(this);
		mainParameter.setMaxLines(1);
		mainParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
		mainParameter.setTextSize(16.0f);
		mainParameter.setWidth(80);
		mainParameter.setTextColor(Color.rgb(0,0,0));
		mainParameter.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
		mainParameter.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		TextView dotText = new TextView(this);
		dotText.setText(".");
		dotText.setTextColor(Color.rgb(0,0,0));
		dotText.setTextSize(16.0f);
		subParameter =new EditText(this);
		subParameter.setMaxLines(1);
		subParameter.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] _inputFilter = new InputFilter[1];
		_inputFilter[0] = new InputFilter.LengthFilter(2);
		subParameter.setFilters(_inputFilter);
		subParameter.setTextSize(16.0f);
		subParameter.setTextColor(Color.rgb(0,0,0));
		subParameter.setWidth(48);
		subParameter.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		TextView uSvhText = new TextView(this);
		uSvhText.setText("μSv/h");
		uSvhText.setTextColor(Color.rgb(0,0,0));
		uSvhText.setTextSize(16.0f);
		
		// レイアウトへのコンポーネントの追加 (4)
		parameterLayout.addView(mainParameter);
		parameterLayout.addView(dotText);
		parameterLayout.addView(subParameter);
		parameterLayout.addView(uSvhText);
		parameterLayout.addView(makeButton("登録","0"));
		layout.addView(parameterLayout);

		// テキストビューの生成 (2)
		countText = new TextView(this);
		countText.setText("回数:0");
		countText.setTextColor(Color.rgb(0,0,0));
		countText.setTextSize(16.0f);
		layout.addView(countText);
		averageText = new TextView(this);
		averageText.setText("平均値:?");
		averageText.setTextColor(Color.rgb(0,0,0));
		averageText.setTextSize(16.0f);
		layout.addView(averageText);
		centralText = new TextView(this);
		centralText.setText("中央値:?");
		centralText.setTextColor(Color.rgb(0,0,0));
		centralText.setTextSize(16.0f);
		layout.addView(centralText);
		maxText =new TextView(this) ;
		maxText.setText("最大値:?");
		maxText.setTextColor(Color.rgb(0,0,0));
		maxText.setTextSize(16.0f);
		layout.addView(maxText);
		minText = new TextView(this);
		minText.setText("最小値:?");
		minText.setTextColor(Color.rgb(0,0,0));
		minText.setTextSize(16.0f);
		layout.addView(minText);
		
		// ボタンレイアウトの生成 (2)
		LinearLayout coefficientLayout = new LinearLayout(this);
		coefficientLayout.setOrientation(LinearLayout.HORIZONTAL);
		coefficientCheck = new CheckBox(this);
		coefficientCheck.setText("係数有効");
		coefficientCheck.setTextColor(Color.BLACK);
		coefficientCheck.setChecked(pref.getBoolean("Coefficient", false));
		coefficientCheck.setOnClickListener(this);
		coefficientMajor =new EditText(this);
		coefficientMajor.setMaxLines(1);
		coefficientMajor.setInputType(InputType.TYPE_CLASS_NUMBER);
		coefficientMajor.setTextSize(16.0f);
		coefficientMajor.setTextColor(Color.rgb(0,0,0));
		coefficientMajor.setText(pref.getString("Major", "0"));
		coefficientMajor.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		coefficientMinor =new EditText(this);
		coefficientMinor.setMaxLines(1);
		coefficientMinor.setInputType(InputType.TYPE_CLASS_NUMBER);
		coefficientMinor.setTextSize(16.0f);
		coefficientMinor.setTextColor(Color.rgb(0,0,0));
		InputFilter[] _coefficientFilter = new InputFilter[1];
		_coefficientFilter[0] = new InputFilter.LengthFilter(3);
		coefficientMinor.setFilters(_coefficientFilter);
		coefficientMinor.setText(pref.getString("Minor", "772"));
		coefficientMinor.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		dotText = new TextView(this);
		dotText.setText(".");
		dotText.setTextColor(Color.rgb(0,0,0));
		dotText.setTextSize(16.0f);
		coefficientLayout.addView(coefficientCheck);		
		coefficientLayout.addView(coefficientMajor);
		coefficientLayout.addView(dotText);
		coefficientLayout.addView(coefficientMinor);
		layout.addView(coefficientLayout);
		
		// ボタンレイアウトの生成 (2)
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.addView(makeButton("リセット","1"));
		buttonLayout.addView(makeButton("展開","2"));
		buttonLayout.addView(makeButton("記憶","3"));
		buttonLayout.addView(makeButton("読込","4"));
		layout.addView(buttonLayout);
		deployText = new EditText(this);
		deployText.setText("");		
		deployText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		layout.addView(deployText);
	}

	//ボタンの生成(1)
	private Button makeButton(String text, String tag) {
		Button button = new Button(this);
		button.setText(text);
		button.setTag(tag);
		button.setOnClickListener(this);	//ボタンクリックイベントの処理 (2)
		button.setLayoutParams(new LinearLayout.LayoutParams(WC,WC));
		return button;
	}


	@Override
	public void onClick(View view) {
		if(view == coefficientCheck)
		{
			if(list.size() > 0) {
				if(coefficientCheck.isChecked() && (coefficientMajor.length() == 0 || coefficientMinor.length() == 0)) {
					coefficientCheck.setChecked(false);
					soundPool.play(soundError, 1.0F, 1.0F, 0, 0, 1.0F);
					Toast.makeText(this, "係数が入力されていません", Toast.LENGTH_SHORT).show();
					return;				
				}
				calicurateTotal();					
			}
		} else {
			int tag = Integer.parseInt((String)view.getTag());
			if(tag == 0) {
				String main = mainParameter.getText().toString();
				String sub = subParameter.getText().toString();
				if(main.length() == 0 || sub.length() == 0) {
					soundPool.play(soundError, 1.0F, 1.0F, 0, 0, 1.0F);
					Toast.makeText(this, "数値が入力されていません", Toast.LENGTH_SHORT).show();
					return;
				}
				if(coefficientCheck.isChecked() && (coefficientMajor.length() == 0 || coefficientMinor.length() == 0)) {
					soundPool.play(soundError, 1.0F, 1.0F, 0, 0, 1.0F);
					Toast.makeText(this, "係数が入力されていません", Toast.LENGTH_SHORT).show();
					return;				
				}
				String parameter = main + "." + sub;
			
				//入力
				Double newarg = Double.valueOf(parameter);
				list.add(newarg);
				Collections.sort(list);
				calicurateTotal();
				soundPool.play(soundOk, 1.0F, 1.0F, 0, 0, 1.0F);
				subParameter.setText("");
			}
			else if(tag == 1) {
				resetParameter();
			}
			else if(tag == 2) {
				if(list.size() > 0) {
					deployText.setText(
							countText.getText() + ", " +
							averageText.getText() + ", " +
							centralText.getText() + ", " +
							maxText.getText() + ", " +
							minText.getText()
					);
				} else {
					Toast.makeText(this, "データが入力されていません", Toast.LENGTH_SHORT).show();
				}
			}
			else if(tag == 3) {
				//プリファレンスへの書き込み
				String parameter = "";
				if(list.size() > 0) {
					for(int i = 0; i < list.size(); i++) {
						parameter += list.get(i);
						if(i + 1 < list.size()) {
							parameter += ",";
						}
					}
				}
								
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Parameter", parameter);
				editor.putString("Major", coefficientMajor.getText().toString());
				editor.putString("Minor", coefficientMinor.getText().toString());
				editor.putString("Deploy", deployText.getText().toString());
				editor.putBoolean("Coefficient", coefficientCheck.isChecked());
				editor.commit();
			}
			else if(tag == 4) {
				//プリファレンスからの読み込み
				coefficientMajor.setText(pref.getString("Major", "0"));
				coefficientMinor.setText(pref.getString("Minor", "772"));
				deployText.setText(pref.getString("Deploy", ""));
				String parameter = pref.getString("Parameter", "");
				if(parameter != "")
				{
					String rawArray[] = parameter.split(",");
					list = new ArrayList<Double>();
					for(int i = 0; i < rawArray.length; i++) {
						Double arg = Double.valueOf(rawArray[i]);
						list.add(arg);
					}
					calicurateTotal();		
				}
				else {
					resetParameter();
				}
			}
		}
	}

	private void resetParameter() {
		list = new ArrayList<Double>();
		mainParameter.setText("");
		subParameter.setText("");
		countText.setText("回数:0");
		averageText.setText("平均値:?");
		centralText.setText("中央値:?");
		maxText.setText("最大値:?");
		minText.setText("最小値:?");
	}

	private void calicurateTotal() {
		countText.setText("回数:" + Integer.toString(list.size()));
		double total = 0;
		for(int i = 0; i < list.size(); i++) {
			total += list.get(i);
		}
		double average = total / list.size();

		double central = list.get(list.size() / 2).doubleValue();
		double maximum = list.get(list.size() - 1).doubleValue();
		double minimum = list.get(0).doubleValue();


		if(coefficientCheck.isChecked()) {
			String major = coefficientMajor.getText().toString();
			String minor = coefficientMinor.getText().toString();
			String temp = major + "." + minor;
			double coefficient = Double.parseDouble(temp);
			average *= coefficient;
			central = roundup(central * coefficient);
			maximum = roundup(maximum * coefficient);
			minimum = roundup(minimum * coefficient);
		}			
		average = roundup(average);

		averageText.setText("平均値:" + Double.toString(average));
		centralText.setText("中央値:" + Double.toString(central));
		maxText.setText("最大値:" + Double.toString(maximum));
		minText.setText("最小値:" + Double.toString(minimum));
	}
	private double roundup(double original) {
		BigDecimal bi = new BigDecimal(String.valueOf(original));
		//小数第四位で四捨五入
		return (bi.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
}