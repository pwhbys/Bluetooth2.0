package com.WideMouth.bluetooth20.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WideMouth.bluetooth20.Activity.BoardActivity;
import com.WideMouth.bluetooth20.Activity.CommunicationActivity;
import com.WideMouth.bluetooth20.ItemClass.Project;
import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMCommonListItemView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectFragment extends Fragment {

    private static final String TAG = "ProjectFragment";

    public static final String defaultProject = "Project of WideMouth";

    public static final String projectName = "PROJECT_NAME";
    public static final String projectId = "PROJECT_ID";

    RecyclerView projectRecyclerView;
    ProjectAdapter projectAdapter;

    static ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addMenuItem:
                        addAction();
                        break;
                    case R.id.settings:
                        Intent intent = new Intent(getContext(), BoardActivity.class)
                                .putExtra(BoardFragment.TITTLE, "设置")
                                .putExtra(BoardFragment.FRAGMENTINDEX, 0);
                        startActivity(intent);
                        break;


                }
                return false;
            }
        });
        projectRecyclerView = view.findViewById(R.id.projectRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        projectRecyclerView.setLayoutManager(layoutManager);
        projectAdapter = new ProjectAdapter();
        projectRecyclerView.setAdapter(projectAdapter);

        imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(WMUtil.settings.getImageRes());
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void goCommunicateActivity(Project project) {
        Intent intent = new Intent(getContext(), CommunicationActivity.class);
        intent.putExtra(projectName, project.getName());
        intent.putExtra(projectId, project.getId());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void addAction() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("创建项目")
                .setPlaceholder("在此输入项目名")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(0, "取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "创建", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String text = builder.getEditText().getText().toString();
                        if (TextUtils.isEmpty(text)) {
                            Toast.makeText(getContext(), "请输入项目名！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            List<Project> projectList = LitePal.where("name = ?", text).find(Project.class);
                            if (projectList != null && projectList.size() > 0) {
                                Toast.makeText(getContext(), "项目重名！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Project project = new Project();

                        project.setName(text);

                        Calendar calendar = Calendar.getInstance();
                        project.setDate(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));

                        project.save();
                        projectAdapter.addProject(project);
                        dialog.dismiss();
                    }
                })
                .create(R.style.Dialog).show();


//        final EditText editText = new EditText(getContext());
//        int p = WMUtil.dip2px(getContext(), 10);
//        editText.setPadding(p, 0, p, 0);
//        SpannableString title = new SpannableString("创建项目");
//        title.setSpan(new AbsoluteSizeSpan(18, true), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        SpannableString negativeText = new SpannableString("取消");
//        negativeText.setSpan(new ForegroundColorSpan(getContext().getColor(R.color.black)), 0, negativeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        new AlertDialog.Builder(getContext())
//                .setView(editText)
//                .setTitle(title)
//                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (TextUtils.isEmpty(editText.getText())) {
//                            Toast.makeText(getContext(), "项目名为空！", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            List<Project> projectList = LitePal.where("name = ?", editText.getText().toString()).find(Project.class);
//                            if (projectList != null && projectList.size() > 0) {
//                                Toast.makeText(getContext(), "项目重名！", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                        Project project = new Project();
//
//                        project.setName(editText.getText().toString());
//
//                        Calendar calendar = Calendar.getInstance();
//                        project.setDate(calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + 1 + "." + calendar.get(Calendar.DAY_OF_MONTH));
//
//                        project.save();
//                        projectAdapter.addProject(project);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }


    public Project newProject() {
        Calendar calendar = Calendar.getInstance();
        Project project = new Project();
        project.setName(defaultProject);
        project.setDate(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
        project.save();
        return project;
    }


    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
        List<Project> projectList;

        public ProjectAdapter() {
            this.projectList = LitePal.findAll(Project.class);
            if (WMUtil.settings.isCreateProject()) {
                if (projectList == null || projectList.size() == 0) {
                    projectList.add(newProject());
                }
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Project p = projectList.get(viewHolder.getAdapterPosition());
                    goCommunicateActivity(p);
                }
            });
            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = viewHolder.getAdapterPosition();
                    final Project p = projectList.get(position);
                    PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.project_edit_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.open:
                                    goCommunicateActivity(p);
                                    break;
                                case R.id.rename:
                                    editAction(p);
                                    break;
                                case R.id.delete:
                                    new QMUIDialog.MessageDialogBuilder(getActivity())
                                            .setTitle("项目删除")
                                            .setMessage("确定要删除吗？")
                                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    p.delete();
                                                    projectList.remove(position);
                                                    notifyItemRemoved(position);
                                                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create(R.style.Dialog).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            return viewHolder;
        }

        public Project editAction(final Project project) {
            final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
            builder.setTitle("项目重命名")
                    .setDefaultText(project.getName())
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction(0, "完成", QMUIDialogAction.ACTION_PROP_POSITIVE,
                            new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    String text = builder.getEditText().getText().toString();
                                    if (TextUtils.isEmpty(text)) {
                                        Toast.makeText(getContext(), "请输入项目名！", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        List<Project> projectList = LitePal.where("name = ?", text).find(Project.class);
                                        if (projectList != null && projectList.size() > 0) {
                                            Toast.makeText(getContext(), "项目重名！", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    project.setName(text);
                                    project.save();
                                    notifyItemChanged(projectList.indexOf(project));
                                    dialog.dismiss();
                                }
                            })
                    .create(R.style.Dialog).show();
            return project;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: ");
            Project project = projectList.get(position);
            SpannableStringBuilder s = new SpannableStringBuilder();
            s.append(project.getName());
            s.append("\n创建于");
            s.append(project.getDate());
            s.setSpan(new ForegroundColorSpan(0xFF858C96), project.getName().length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.textView.setText(s);
        }

        public void addProject(Project project) {
            projectList.add(project);
            notifyItemInserted(projectList.size() - 1);
            projectRecyclerView.scrollToPosition(getItemCount() - 1);
        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            ImageButton editButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.project_info);
                editButton = itemView.findViewById(R.id.editButton);
            }
        }
    }
}