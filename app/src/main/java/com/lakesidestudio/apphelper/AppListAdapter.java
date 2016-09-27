package com.lakesidestudio.apphelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.service.notification.Condition.SCHEME;


/**
 * Created by blackkensai on 16-9-24.
 */

public class AppListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private AppHelper appHelper;
    private List<ApplicationInfo> packages;
    private LayoutInflater inflater = null;
    private volatile boolean inited = false;
    private AppListFilter appListFilter;

    public AppListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.appHelper = new AppHelper(context);
    }

    private void init() {
        if (inited) {
            return;
        }

        packages = new ArrayList<>(100);
        for (ApplicationInfo info :
                context.getPackageManager()
                        .getInstalledApplications(PackageManager.GET_META_DATA)) {
            packages.add(info);
        }
        Collections.sort(packages, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo a0, ApplicationInfo a1) {
                return a0.packageName.compareTo(a1.packageName);
            }
        });
        inited = true;

    }

    @Override
    public int getCount() {
        init();
        return packages.size();
    }

    @Override
    public Object getItem(int i) {
        return packages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return packages.get(i).uid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_view, null);
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.info = (TextView) view.findViewById(R.id.info);

            view.setTag(holder);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("ApplicationInfo", ((ViewHolder) view.getTag()).applicationInfo);
                    context.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.applicationInfo = packages.get(i);
        holder.img.setImageDrawable(appHelper.getAppIcon(packages.get(i)));
        holder.title.setText(appHelper.getAppName(packages.get(i)));
        holder.info.setText(packages.get(i).packageName);

        return view;
    }

    @Override
    public Filter getFilter() {
        if (appListFilter == null) {
            appListFilter = new AppListFilter();
        }
        return appListFilter;
    }


    static class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView info;
        public ApplicationInfo applicationInfo;
    }


    public class AppListFilter extends Filter {
        private Boolean system;
        private Boolean game;

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<ApplicationInfo> filteredPackages = new ArrayList<>();
            boolean needFilterName = charSequence != null && charSequence.length() != 0;
            for (ApplicationInfo applicationInfo : context.getPackageManager()
                    .getInstalledApplications(PackageManager.GET_META_DATA)
                    ) {
                if (system != null && system != appHelper.isSystemApp(applicationInfo)) {
                    continue;
                }
                if (game != null && game != appHelper.isGame(applicationInfo)) {
                    continue;
                }
                if (needFilterName) {
                    if (!appHelper.getAppName(applicationInfo).toString().contains(charSequence) &&
                            !applicationInfo.packageName.contains(charSequence)) {
                        continue;
                    }
                }
                filteredPackages.add(applicationInfo);
            }
            Collections.sort(filteredPackages, new Comparator<ApplicationInfo>() {
                @Override
                public int compare(ApplicationInfo a0, ApplicationInfo a1) {
                    return a0.packageName.compareTo(a1.packageName);
                }
            });
            filterResults.count = filteredPackages.size();
            filterResults.values = filteredPackages;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            packages = (List<ApplicationInfo>) filterResults.values;
            notifyDataSetChanged();
        }

        public Boolean getGame() {
            return game;
        }

        public void setGame(Boolean game) {
            this.game = game;
        }

        public Boolean getSystem() {
            return system;
        }

        public void setSystem(Boolean system) {
            this.system = system;
        }
    }
}
