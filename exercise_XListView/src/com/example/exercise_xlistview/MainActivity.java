package com.example.exercise_xlistview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.exercise_xlistview.views.XListView;
import com.example.exercise_xlistview.views.XListView.IXListViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements IXListViewListener{

	private XListView xlistView;
	private ArrayList<String> list;
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//连接到控件
		xlistView = (XListView) findViewById(R.id.xlistView);
		//创建集合存放模拟数据
		list = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			list.add("条目"+i);
		}
		//XListView设置适配器
		myAdapter = new MyAdapter();
		xlistView.setAdapter(myAdapter);
		//打开xlistView的开关
		xlistView.setPullLoadEnable(true);//可加载更多
		xListView.setPullRefreshEnable(true);//可刷新
		//xlistView设置监听
		xlistView.setXListViewListener(this);
	}
	
	//创建适配器
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(MainActivity.this);
			tv.setText(list.get(position));
			return tv;
		}
		
	}

	//开启Handler，在主线程更新UI
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			//调用格式化时间的方法，获取当前时间
			String time = formatDate();
			//给xlistView设置刷新时间
			xlistView.setRefreshTime(time);
			//刷新适配器
			myAdapter.notifyDataSetChanged();
			//调用onLoad方法，让xlistView停止刷新或者加载
			onLoad();
		};
	};
	
	//重写onLoad方法
	public void onLoad(){
		//停止刷新
		xlistView.stopRefresh();
		//停止加载更多
		xlistView.stopLoadMore();
	}
	
	//重写IXListViewListener接口的方法
	//下拉刷新
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		//模拟数据
		//开启子线程，模仿网络请求数据（因为网络请求要在子线程操作），
		new Thread(){
			public void run() {
				try {
					sleep(2000);//休眠一下，类似于向网络请求数据时要需要一点时间
					list.add("下拉刷新");//每刷新一次，向集合里添加一条数据，当做是从网络请求来的数据
					handler.sendEmptyMessage(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	//上拉加载
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				try {
					sleep(2000);
					list.add(0, "上拉加载");
					handler.sendEmptyMessage(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	// 格式化时间的方法
	private String formatDate() {
		// TODO Auto-generated method stub
		//获取系统当前时间
		Date date = new Date();
		// 得到SimpleDateFormat对象，也就是说要把你的时间格式化成什么样式
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 对时间进行格式化
		final String format = simpleDateFormat.format(date);
		// 返回这个格式化后的时间
		return format;
	}
}
