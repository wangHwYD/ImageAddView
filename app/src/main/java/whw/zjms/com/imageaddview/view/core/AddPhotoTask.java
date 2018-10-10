package whw.zjms.com.imageaddview.view.core;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import whw.zjms.com.imageaddview.view.adapter.AddPhotoAdapter;

/**
 * @author whw
 */
public class AddPhotoTask<T> extends AsyncTask<Void, Void, List<String>> {

    public AddPhotoTask(AddPhotoAdapter adapter) {
        mAdapter = adapter;
    }

    private AddPhotoAdapter mAdapter;
    private int size;

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("");
        }
        return list;
    }

    public void setNewData(int size) {
        this.size = size;
        execute();
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        mAdapter.setNewData(strings);
    }

    public void remove() {
        mAdapter = null;
        cancel(true);
    }
}
