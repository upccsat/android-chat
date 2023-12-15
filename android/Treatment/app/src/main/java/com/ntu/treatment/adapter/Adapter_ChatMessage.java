package com.ntu.treatment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ntu.treatment.R;
import com.ntu.treatment.modle.ChatMessage;
/*import com.yxc.websocketclientdemo.R;
import com.yxc.websocketclientdemo.modle.ChatMessage;*/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter_ChatMessage extends BaseAdapter {
    List<ChatMessage> mChatMessageList;
    LayoutInflater inflater;
    Context context;

    public Adapter_ChatMessage(Context context, List<ChatMessage> list) {
        this.mChatMessageList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatMessageList.get(position).getIsMeSend() == 1)
            return 0;// 返回的数据位角标
        else
            return 1;
    }

    @Override
    public int getCount() {
        return mChatMessageList.size();
    }


    @Override
    public Object getItem(int i) {
        return mChatMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage mChatMessage = mChatMessageList.get(i);
        String content = mChatMessage.getContent();
        String time = formatTime(mChatMessage.getTime());
        int isMeSend = mChatMessage.getIsMeSend();
        String fromUserName=mChatMessage.getFromUserName();
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            if (isMeSend == 0) {//对方发送
                view = inflater.inflate(R.layout.item_chat_receive_text, viewGroup, false);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);
                holder.tv_display_name = view.findViewById(R.id.tv_display_name);
                holder.tv_display_name.setText(fromUserName);
                holder.tv_display_name.setVisibility(View.VISIBLE);
            } else {
                view = inflater.inflate(R.layout.item_chat_send_text, viewGroup, false);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);

            }

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        holder.tv_sendtime.setText(time);
        holder.tv_content.setVisibility(View.VISIBLE);
        holder.tv_content.setText(content);


        //如果是自己发送才显示未读已读

        return view;
    }

    class ViewHolder {
        private TextView tv_content, tv_sendtime, tv_display_name, tv_isRead;
    }


    /**
     * 将毫秒数转为日期格式
     *
     * @param timeMillis
     * @return
     */
    private String formatTime(String timeMillis) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = inputDateFormat.parse(timeMillis);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parse exception as needed
            return null; // Return an appropriate value, or throw an exception
        }
    }
}
