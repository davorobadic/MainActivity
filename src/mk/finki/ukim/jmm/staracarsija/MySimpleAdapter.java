package mk.finki.ukim.jmm.staracarsija;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] values;

  public MySimpleAdapter(Context context, String[] values) {
    super(context, R.layout.activity_main , values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.activity_main, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView.setText(values[position]);
    
    String s = values[position];
    if (s.startsWith("Ресторани")) {
      imageView.setImageResource(R.drawable.restoran);
      textView.setTextColor(Color.RED);
    }
   /* else if (s.startsWith("Златари")) {
        imageView.setImageResource(R.drawable.gold);
        textView.setTextColor(Color.RED);
      }*/
    else if (s.startsWith("Споменици")) {
        imageView.setImageResource(R.drawable.spomenik);
        textView.setTextColor(Color.GREEN);
      }
    else if (s.startsWith("Занаетчии")) {
        imageView.setImageResource(R.drawable.zanaetcii50);
        textView.setTextColor(Color.argb(255, 237, 145, 33));
      }
    else if (s.startsWith("Кафулиња")) {
      imageView.setImageResource(R.drawable.kafe);
      textView.setTextColor(Color.BLUE);
    } else  if (s.startsWith("Останато")){
    	imageView.setImageResource(R.drawable.ic_launcher);
        textView.setTextColor(Color.YELLOW);
    }

    return rowView;
  }
} 