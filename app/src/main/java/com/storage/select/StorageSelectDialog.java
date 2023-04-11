
package com.storage.select;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StorageSelectDialog extends Dialog {

    private Context mContext;
    private LinearLayout ll_internal_storage, ll_external_storage;
    private String strTitle;
    private TextView tvTitle, txt_select_path;
    private TextView mInternalSpaceTv, mExternalSpaceTv;
    private ImageView iv_internal, iv_external;
    private InfoCallback callback;

    public StorageSelectDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_save_video_position);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        tvTitle = findViewById(R.id.text_dialog_save_video_title);
        txt_select_path = findViewById(R.id.txt_select_path);
        iv_internal = findViewById(R.id.iv_internal);
        iv_external = findViewById(R.id.iv_external);
        mInternalSpaceTv = findViewById(R.id.txt_memory_space);
        mExternalSpaceTv = findViewById(R.id.txt_sdcard_space);

        ll_internal_storage = findViewById(R.id.ll_internal_storage);
        ll_external_storage = findViewById(R.id.ll_external_storage);
        ArrayList<String> storagePaths = StorageManager.instance().getStoragePaths();

        if (storagePaths.size() > 1) {
            mInternalSpaceTv.setText(SDCardUtil.getAvaliableSize(storagePaths.get(0).split("Android")[0]));
            mExternalSpaceTv.setText(SDCardUtil.getAvaliableSize(storagePaths.get(1).split("Android")[0]));
        }else {
            mInternalSpaceTv.setText(SDCardUtil.getAvaliableSize(storagePaths.get(0).split("Android")[0]));
            ll_external_storage.setVisibility(View.GONE);
        }
        //默认选择
        initListener();
    }

    public void setOnInfoCallback(InfoCallback callback) {
        this.callback = callback;
    }

    public void initListener() {
        ll_internal_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectInternalStorage();
                callback.onSelected(ConstantUtils.PATH_INTERNAL_STORAGE);
            }
        });
        ll_external_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectExternalStorage();
                callback.onSelected(ConstantUtils.PATH_EXTERNAL_STORAGE);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        }
        showSelect();
    }

    private void showSelect() {
        int selectPos = (Integer) SPUtils.get(ConstantUtils.KEY_SP_PATH, 0);
        if (ConstantUtils.PATH_INTERNAL_STORAGE == selectPos) {
            selectInternalStorage();
        } else {
            selectExternalStorage();
        }
    }

    public void setDialogTitle(String str) {
        strTitle = str;
    }

    public interface InfoCallback {
        public void onSelected(int position);
    }

    private void selectExternalStorage() {
        iv_internal.setImageResource(R.mipmap.radio_button_unselect);
        iv_external.setImageResource(R.mipmap.radio_button_select);
        txt_select_path.setText(String.format(mContext.getString(R.string.current_download_path), ConstantUtils.PATH_EXTERNAL));
    }

    private void selectInternalStorage() {
        iv_internal.setImageResource(R.drawable.radio_button_select);
        iv_external.setImageResource(R.drawable.radio_button_unselect);
        txt_select_path.setText(String.format(mContext.getString(R.string.current_download_path), ConstantUtils.PATH_INTERNAL));
    }

}
