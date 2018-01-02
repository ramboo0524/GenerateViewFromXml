package com.genview.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.genview.library.ui.ParamIsNotRight;
import com.genview.library.xmlload.XMLParser;

/**
 * @author liangjianhua
 */
public class Demo extends Activity implements OnClickListener {

	private final String TAG = "Demo";

	private Button billButton;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		billButton = (Button) findViewById(R.id.billing);
		billButton.setOnClickListener(this);
//

//		View view = parser.generateConentView();
//		setContentView( view );
////		View button = parser.findViewByID("@+id/bubu1");
////		
////		View.OnClickListener listener = new View.OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				
////				Toast.makeText(Demo.this, "view finded:" + ((TextView)v).getText(), Toast.LENGTH_SHORT).show();
////				
////			}
////		};
////		button.setOnClickListener(listener);
////		button = parser.findViewByID("@+id/textView1");
////		button.setOnClickListener(listener);
////		button = parser.findViewByID("@+id/button3");
////		button.setOnClickListener(listener);
//		
//  		setContentView( R.layout.layout_wanyou );
//		
//		Intent intent = new Intent(this, AnimDemoActivity.class);
//		startActivity(intent);
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.billing:

            XMLParser parser = new XMLParser(this);
            try {
                parser.parseXML(this, "layout_share_dialog.xml");
//			parser.parseXML(this, "test4.xml");
            } catch (ParamIsNotRight e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

			 final AnimDialog dialog = new AnimDialog(this);
//			 dialog.setContentView( View.inflate(this, R.layout.layout_share_dialog, null) );
            dialog.setContentView( parser.generateConentView() );
			 dialog.show();

//			Button btn = (Button) dialog.findViewById(R.id.id_share_cancle );
			Button btn = (Button) parser.findViewByID( "@+id/id_share_cancle" );
			btn.setOnClickListener(new  View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			
			break;
		default:
			break;
		}

	}

}