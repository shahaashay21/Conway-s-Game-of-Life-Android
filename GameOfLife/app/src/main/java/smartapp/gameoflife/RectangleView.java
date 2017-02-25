package smartapp.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by aashayshah on 2/24/17.
 */

public class RectangleView extends View {

    private Rect rectangle;
    private Paint paint;

    public RectangleView(Context context, int x, int y) {
        super(context);

//        int x = 250;
//        int y = 250;
        int sideLength = 100;

        rectangle = new Rect(x, y, x+sideLength, y+sideLength);

        paint = new Paint();
        paint.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rectangle, paint);
    }
}
