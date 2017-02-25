package smartapp.gameoflife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.DrawableContainer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by aashayshah on 2/24/17.
 */

public class RectangleAdapter extends BaseAdapter {

    private Context mContext;

    public RectangleAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
        RectangleView rectangleView;

        if(convertView == null){
            rectangleView = new RectangleView(mContext, 100, 100);
//
//            Paint paint = new Paint();
//            paint.setColor(Color.RED);
//
//            Bitmap bm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bm);
//            canvas.drawCircle(100, 100, 100, paint);
//
//            rectangleView.setBackground(new BitmapDrawable(bm));
//
        }else{
            rectangleView = (RectangleView) convertView;
        }

//        rectangleView.setBackgroundResource(getSource());
        return rectangleView;
    }

    public RectangleView[] getSource(){
        RectangleView data[] = new RectangleView[144];
        int coordition = 100;
        int current = 200;

        for(int i=0; i<144; i++){
            data[i] = new RectangleView(mContext, current, current);
            current+=100;
        }
        return data;
    }
}
