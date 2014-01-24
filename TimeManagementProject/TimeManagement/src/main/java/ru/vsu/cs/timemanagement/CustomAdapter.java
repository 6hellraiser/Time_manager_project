package ru.vsu.cs.timemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Наталья on 12.01.14.
 */
public class CustomAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    List<Data> tasks;

    public CustomAdapter(List<Data> tasks, Context context) {
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Data getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.task_item, viewGroup, false);

            view.setTag(R.id.name, view.findViewById(R.id.name));
            view.setTag(R.id.descr, view.findViewById(R.id.descr));
        }

        Data task = getItem(i);

        ((TextView) view.getTag(R.id.name)).setText(task.name);
        ((TextView) view.getTag(R.id.descr)).setText(task.description);

        return view;
    }

}
