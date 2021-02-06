package com.example.holdon.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.holdon.R;

import java.util.ArrayList;
import java.util.Collections;

public class PlayVideoContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
                                implements OnBookmarkClickListener {

    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<PlayVideoContent> items = new ArrayList<PlayVideoContent>();
    private OnBookmarkClickListener onBookmarkClickListener;

    public PlayVideoContentAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == PlayVideoContent.TYPE_DESCRIPTION) {
            itemView = inflater.inflate(R.layout.play_video_content_description, parent, false);
            return new DescriptionViewHolder(itemView);
        }
        else if (viewType == PlayVideoContent.TYPE_SEPARATOR) {
            itemView = inflater.inflate(R.layout.play_video_content_bookmark_separator, parent, false);
            return new SeparatorViewHolder(itemView);
        }
        else if (viewType == PlayVideoContent.TYPE_BOOKMARK) {
            itemView = inflater.inflate(R.layout.play_video_content_bookmark, parent, false);
            return new BookmarkViewHolder(itemView, this);
        }
        else if (viewType == PlayVideoContent.TYPE_FOOTER) {
            itemView = inflater.inflate(R.layout.play_video_content_footer, parent, false);
            return new FooterViewHolder(itemView);
        }

        itemView = inflater.inflate(R.layout.play_video_content_footer, parent, false);
        return new FooterViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        PlayVideoContent item = items.get(position);
        return item.getContentType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (items != null) {

            int type = getItemViewType(position);
            PlayVideoContent content = items.get(position);
            content.setAdapterIndex(position);

            if (type == PlayVideoContent.TYPE_DESCRIPTION) {
                ((DescriptionViewHolder)holder).setDescriptionContent(content);
            }
            else if (type == PlayVideoContent.TYPE_BOOKMARK) {
                ((BookmarkViewHolder)holder).setBookmarkContent(content);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    @Override
    public void onBookmarkClicked(int position) {
        if (onBookmarkClickListener != null) {
            onBookmarkClickListener.onBookmarkClicked(position);
        }
    }

    @Override
    public void onBookmarkDeleteClicked(int position) {
        if (onBookmarkClickListener != null) {
            onBookmarkClickListener.onBookmarkDeleteClicked(position);
        }
    }

    @Override
    public void onBookmarkNameEdited(int position, String editedName) {
        if (onBookmarkClickListener != null) {
            onBookmarkClickListener.onBookmarkNameEdited(position, editedName);
        }
    }

    @Override
    public void onBookmarkBreakpointClicked(int position) {
        if (onBookmarkClickListener != null) {
            onBookmarkClickListener.onBookmarkBreakpointClicked(position);
        }
    }

    @Override
    public void onBookmarkRepeatClicked(int position) {
        if (onBookmarkClickListener != null) {
            onBookmarkClickListener.onBookmarkRepeatClicked(position);
        }
    }

    public ArrayList<PlayVideoContent> getItems() {
        return items;
    }

    public void setItems(ArrayList<PlayVideoContent> items) {
        this.items = items;
    }

    public PlayVideoContent getItem(int position) {
        return items.get(position);
    }

    public void addItem(PlayVideoContent item) {
        items.add(item);
    }

    public void addBookmarkItem(PlayVideoContent bookmarkItem) {
        for (int i = 0; i < items.size(); i++) {
            PlayVideoContent item = items.get(i);
            if (item.getContentType() != PlayVideoContent.TYPE_BOOKMARK)
                continue;

            if (bookmarkItem.getBookmarkTime() < item.getBookmarkTime()) {
                addItemTo(i, bookmarkItem);
                return;
            }
        }
        addItemTo(items.size() - 1, bookmarkItem);
    }

    public void addItemTo(int index, PlayVideoContent item) {
        items.add(index, item);
    }

    public void addItems(ArrayList<PlayVideoContent> contents) {
        items.addAll(contents);
    }

    public void setItem(int position, PlayVideoContent item) {
        items.set(position, item);
    }

    public void deleteItem(int position) {
        items.remove(position);
    }

    public PlayVideoContent getBookmarkItemFromID(int _id) {
        PlayVideoContent ret = null;
        for (PlayVideoContent item : items) {
            if (item.getContentType() == PlayVideoContent.TYPE_BOOKMARK
                    && item.getBookmarkNumber() == _id) {
                ret = item;
            }
        }
        return ret;
    }

    public PlayVideoContent getBookmarkItem(int position) {
        position--;   // 보여지는 북마크 번호는 1부터, 실제 인덱싱은 0부터
        position += 2;    // description 이랑 separator
        PlayVideoContent ret = null;
        for (PlayVideoContent item : items) {
            if (item.getContentType() == PlayVideoContent.TYPE_BOOKMARK
                    && item.getAdapterIndex() == position) {
                ret = item;
            }
        }
        return ret;
    }

    public void sortItems() {
        Collections.sort(items);
    }

    public static class DescriptionViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout playVideoContentDescriptionLayout;
        private final TextView playVideoContentDescriptionTitle;
        private final TextView playVideoContentDescriptionText;
        private final ImageView playVideoContentDescriptionMagnifierImage;

        private boolean isExtended = false;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            playVideoContentDescriptionLayout = itemView.findViewById(R.id.play_video_content_description_layout);
            playVideoContentDescriptionTitle = itemView.findViewById(R.id.play_video_content_description_title);
            playVideoContentDescriptionText = itemView.findViewById(R.id.play_video_content_description_text);
            playVideoContentDescriptionMagnifierImage = itemView.findViewById(R.id.play_video_content_description_magnifier_image);

            playVideoContentDescriptionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isExtended) {
                        playVideoContentDescriptionTitle.setMaxLines(2);
                        playVideoContentDescriptionText.setVisibility(TextView.GONE);
                    }
                    else {
                        playVideoContentDescriptionTitle.setMaxLines(1000);
                        playVideoContentDescriptionText.setVisibility(TextView.VISIBLE);
                    }
                    isExtended = !isExtended;
                }
            });
        }

        public void setDescriptionContent(PlayVideoContent content) {
            playVideoContentDescriptionTitle.setText(content.getTitle());
            playVideoContentDescriptionText.setText(content.getDescription());
        }
    }

    public static class SeparatorViewHolder extends RecyclerView.ViewHolder {

        public SeparatorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

        private final CardView playVideoContentBookmarkCard;
        private final TextView playVideoContentBookmarkNumber;
        private final TextView playVideoContentBookmarkTime;
        private final TextView playVideoContentBookmarkDescription;
        private final EditText playVideoContentBookmarkEdit;
//        private ImageButton breakpointButton;
//        private ImageButton repeatButton;


        //        private boolean isRepeatActivated = false;
//        private boolean isBPActivated = false;
        private boolean isEditBookmarkActivated = false;

        public BookmarkViewHolder(@NonNull View itemView, final OnBookmarkClickListener listener) {
            super(itemView);

            playVideoContentBookmarkCard = itemView.findViewById(R.id.play_video_content_bookmark_card);
            playVideoContentBookmarkNumber = itemView.findViewById(R.id.play_video_content_bookmark_number);
            playVideoContentBookmarkTime = itemView.findViewById(R.id.play_video_content_bookmark_time);
            playVideoContentBookmarkDescription = itemView.findViewById(R.id.play_video_content_bookmark_description);
            playVideoContentBookmarkEdit = itemView.findViewById(R.id.play_video_content_bookmark_edit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onBookmarkClicked(position);
                    }
                }
            });

            final ImageButton editBookmarkButton = itemView.findViewById(R.id.play_video_content_bookmark_edit_button);
            editBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditBookmarkActivated) {
                        isEditBookmarkActivated = false;
                        String editedName = playVideoContentBookmarkEdit.getText().toString();
                        playVideoContentBookmarkDescription.setText(editedName);

                        playVideoContentBookmarkEdit.setVisibility(View.GONE);
                        playVideoContentBookmarkDescription.setVisibility(View.VISIBLE);

                        int position = getAdapterPosition();
                        if (listener != null) {
                            listener.onBookmarkNameEdited(position, editedName);
                        }
                    } else {
                        isEditBookmarkActivated = true;
                        String name = playVideoContentBookmarkDescription.getText().toString();
                        playVideoContentBookmarkEdit.setText(name);

                        playVideoContentBookmarkDescription.setVisibility(View.GONE);
                        playVideoContentBookmarkEdit.setVisibility(View.VISIBLE);
                        playVideoContentBookmarkEdit.requestFocus();
                    }
                    editBookmarkButton.setActivated(isEditBookmarkActivated);
                }
            });

            ImageButton deleteBookmarkButton = itemView.findViewById(R.id.play_video_content_bookmark_delete_button);
            deleteBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("안내")
                            .setMessage("이 북마크를 제거하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    if (listener != null) {
                                        listener.onBookmarkDeleteClicked(position);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

//            breakpointButton = itemView.findViewById(R.id.breakpointButton);
//            breakpointButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (listener != null) {
//                        listener.onBookmarkBreakpointClicked(position);
//                    }
//
//                    processBPActivation();
//                }
//            });
//
//            repeatButton = itemView.findViewById(R.id.repeatButton);
//            repeatButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (listener != null) {
//                        listener.onBookmarkRepeatClicked(position);
//                    }
//
//                    processRepeatActivation();
//                }
//            });
        }

//        private void processBPActivation() {
//            isBPActivated = !isBPActivated;
//            breakpointButton.setActivated(isBPActivated);
//        }
//
//        private void processRepeatActivation() {
//            isRepeatActivated = !isRepeatActivated;
//            repeatButton.setActivated(isRepeatActivated);
//        }

        public void setBookmarkContent(PlayVideoContent content) {
            // description, separator 이후에 북마크 나오기 때문에 -2
            // 1부터 인덱싱 하고 싶어서 + 1
            playVideoContentBookmarkNumber.setText(String.valueOf(content.getAdapterIndex() - 2 + 1));
            playVideoContentBookmarkTime.setText(formattingBookmarkTime(content.getBookmarkTime() / 1000));
            playVideoContentBookmarkDescription.setText(content.getBookmarkDescription());
        }

        private String formattingBookmarkTime(int time) {
            int hour = time / 3600;
            time %= 3600;
            int minute = time / 60;
            time %= 60;
            int second = time;
            return String.format("%d:%d:%d", hour, minute, second);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
