package zakharov.nikolay.com.androidlvl3homework6;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hari on 20/11/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    List<Model> models;
    ListView mListView;


    public UserAdapter(ListView listView, List<Model> dictionaries) {
        this.models = dictionaries;
        this.mListView = listView;
        notifyDataSetChanged();
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new UserHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        final Model model = models.get(holder.getAdapterPosition());
        mListView.setImage(holder.avatar, model.getAvatar());
        holder.usersName.setText(model.getLogin());
        holder.avatar.setOnClickListener(view -> {
            mListView.startUserFragment(model);
        });
        holder.usersName.setOnClickListener(view -> {
            mListView.startUserFragment(model);
        });
    }

    @Override
    public int getItemCount() {
        if (models != null && models.size() != 0) {
            return models.size();
        }
        return 0;
    }

    public void setItems(List<Model> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    static class UserHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView usersName;

        public UserHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_list_item);
            usersName = (TextView) itemView.findViewById(R.id.user_name_list_item);
        }
    }
}