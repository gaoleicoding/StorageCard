package com.storage.select;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private TextView mDowloadPosTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout mDownloadPosRl = findViewById(R.id.rl_download_pos);
        mDowloadPosTv = findViewById(R.id.tv_storage_pos);
        getPermission();
        int selectPos = (Integer) SPUtils.get(ConstantUtils.KEY_SP_PATH, 0);
        if (ConstantUtils.PATH_INTERNAL_STORAGE == selectPos) {
            mDowloadPosTv.setText(getResources().getString(R.string.text_memory_card));
        } else {
            mDowloadPosTv.setText(getResources().getString(R.string.text_sd_card));
        }

        mDownloadPosRl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StorageSelectDialog dialog = new StorageSelectDialog(
                        MainActivity.this, R.style.DefaultDialog);

                dialog.setDialogTitle(getResources().getString(
                        R.string.text_position_storage_video));

                dialog.setOnInfoCallback(new StorageSelectDialog.InfoCallback() {

                    @Override
                    public void onSelected(int position) {
                        if (position == ConstantUtils.PATH_INTERNAL_STORAGE) {
                            mDowloadPosTv.setText(getString(R.string.text_memory_card));
                        }
                        if (position == ConstantUtils.PATH_EXTERNAL_STORAGE) {
                            mDowloadPosTv.setText(getString(R.string.text_sd_card));
                        }
                    }

                });
                dialog.show();
            }
        });
    }

    private void getPermission() {
        XXPermissions.with(this)
                // 申请单个权限
                // 申请多个权限
                .permission(Permission.Group.STORAGE)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {

                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        }
                    }
                });
    }


}
