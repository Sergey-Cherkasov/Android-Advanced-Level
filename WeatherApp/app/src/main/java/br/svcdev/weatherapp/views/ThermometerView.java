package br.svcdev.weatherapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import br.svcdev.weatherapp.R;

public class ThermometerView extends View {

	private static int sWidth = 30;
	private static int sHeight = 90;

	public static int warmTempColor;
	public static int coldTempColor;

	private Paint thermometerPaint;
	private Path thermometerPath = new Path();
	private static final int PADDING = 10;

	public ThermometerView(Context context) {
		super(context);
		init();
	}

	public ThermometerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
		init();
	}

	public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		init();
	}

	public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initAttrs(context, attrs);
		init();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThermometerView,
				0, 0);

		warmTempColor = typedArray.getColor(R.styleable.ThermometerView_warmTemperature_color,
				Color.RED);
		coldTempColor = typedArray.getColor(R.styleable.ThermometerView_coldTemperature_color,
				Color.BLUE);

		sHeight = typedArray.getInt(R.styleable.ThermometerView_android_layout_height, 90);
		sWidth = typedArray.getInt(R.styleable.ThermometerView_android_layout_width, 30);

		typedArray.recycle();
	}

	private void init() {
		thermometerPaint = new Paint();
		thermometerPaint.setColor(Color.BLACK);
		thermometerPaint.setStyle(Paint.Style.STROKE);
		thermometerPaint.setStrokeWidth(4);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		sWidth = w - getPaddingLeft() - getPaddingRight();
		sHeight = h - getPaddingTop() - getPaddingBottom();

		if (sWidth <= sHeight && sWidth >= 30 && sHeight >= 90) {
			createThermometerPath();
			createThermometerValuePath();
			createThermometerLevelPath();
		} else {
			throw new RuntimeException("Wrong values of sizes view: width, height");
		}
	}

	private void createThermometerLevelPath() {

	}

	private void createThermometerValuePath() {

	}

	private void createThermometerPath() {
		// Настраиваем верхнюю дугу
		RectF smallOval = new RectF();
		Point smallOvalLeftTopPoint = new Point();
		Point smallOvalRightBottomPoint = new Point();
		smallOvalLeftTopPoint.x = (sWidth >> 2) + PADDING;
		smallOvalLeftTopPoint.y = PADDING;
		smallOvalRightBottomPoint.x = sWidth - (sWidth >> 2) - PADDING;
		smallOvalRightBottomPoint.y = (sWidth >> 1) + PADDING;
		smallOval.set(smallOvalLeftTopPoint.x, smallOvalLeftTopPoint.y,
				smallOvalRightBottomPoint.x, smallOvalRightBottomPoint.y);

		// Настраиваем нижнюю дугу
		RectF bigOval = new RectF();
		Point bigOvalLeftTopPoint = new Point();
		Point bigOvalRightBottomPoint = new Point();
		bigOvalLeftTopPoint.x = PADDING;
		bigOvalLeftTopPoint.y = sHeight - sWidth + PADDING;
		bigOvalRightBottomPoint.x = sWidth - PADDING;
		bigOvalRightBottomPoint.y = sHeight - PADDING;
		bigOval.set(bigOvalLeftTopPoint.x, bigOvalLeftTopPoint.y, bigOvalRightBottomPoint.x,
				bigOvalRightBottomPoint.y);

		float lenghtLine = sHeight - sWidth - (smallOvalRightBottomPoint.y - smallOvalLeftTopPoint.y) / 2;
		Path pointTemps = new Path();


		thermometerPath.arcTo(smallOval, 180, 180);
		thermometerPath.lineTo(smallOvalRightBottomPoint.x, sHeight - sWidth);
		thermometerPath.arcTo(bigOval, 300, 300);
		thermometerPath.lineTo(smallOvalLeftTopPoint.x, sHeight - sWidth);
		thermometerPath.close();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(thermometerPath, thermometerPaint);
	}

}
