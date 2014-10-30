package com.taskmon;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class LineGraph {

	private GraphicalView view;
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();


	public XYMultipleSeriesDataset getmDataset() {
		return mDataset;
	}

	public void setmDataset(XYMultipleSeriesDataset mDataset) {
		this.mDataset = mDataset;
	}

	public XYMultipleSeriesRenderer getmRenderer() {
		return mRenderer;
	}

	public void setmRenderer(XYMultipleSeriesRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}

	public LineGraph(String type)
	{

		// Enable Zoom
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Time");
		mRenderer.setYAxisMin(0);

		
			mRenderer.setYAxisMax(100);

			mRenderer.setScale(1);
		//mRenderer.setYAxisMax(100, 10);
		mRenderer.setYTitle("Utilization");

		mRenderer.setPanEnabled(true, false);
		mRenderer.setZoomEnabled(true, false);

	}

	public void addDataset(XYSeries series)
	{
		mDataset.addSeries(series);		
	}

	public void addRenderer(XYSeriesRenderer renderer)
	{		
		mRenderer.addSeriesRenderer(renderer);	
	}

	public void removeDataset(XYSeries series)
	{
		mDataset.removeSeries(series);		
	}

	public void removeRenderer(XYSeriesRenderer renderer)
	{		
		mRenderer.removeSeriesRenderer(renderer);    				
	}

	public GraphicalView getView(Context context) 
	{
		view =  ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}

}
