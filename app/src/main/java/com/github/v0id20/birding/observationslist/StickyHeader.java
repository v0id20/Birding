package com.github.v0id20.birding.observationslist;

import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.birding.R;

public class StickyHeader extends RecyclerView.ItemDecoration {
    public enum State {INITIAL, SCROLL, MOVE_HEADER, HIDE}

    private State mState = State.INITIAL;
    private View headerCover = null;
    private final StickyHeaderInterface mListener;
    private int currentTopChildPosition = -10;
    private View currentHeader;
    private final String TAG = "Sticky header";
    private boolean wasPreviouslyHeader = false;
    private int contactPoint;

    public StickyHeader(@NonNull StickyHeaderInterface listener) {
        mListener = listener;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View topChild = parent.getChildAt(0);
        if (topChild == null) {
            return;
        }
        int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }

        View childInContact = getChildInContact(parent, contactPoint);

        if (currentTopChildPosition != topChildPosition) {
            currentHeader = getHeaderViewForItem(topChildPosition, parent);
            currentTopChildPosition = topChildPosition;
        }
        if (currentHeader != null) {
            fixLayoutSize(parent, currentHeader);
        }
        switch (mState) {
            case INITIAL:
                //TODO: reuse currentHeader in the beginning of the method
                contactPoint = currentHeader.getBottom();
                wasPreviouslyHeader = true;
                mState = State.HIDE;
                break;
            case SCROLL:
                if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
                    mState = State.MOVE_HEADER;
                    Log.d(TAG, "onDrawOver: STATE: SCROLL");
                    Log.d(TAG, "onDrawOver: ASSIGNED STATE: MOVE HEADER");
                    wasPreviouslyHeader = true;
                } else if (mListener.isHeader(topChildPosition)) {
                    mState = State.HIDE;
                    Log.d(TAG, "onDrawOver: STATE: SCROLL");
                    Log.d(TAG, "onDrawOver: ASSIGNED STATE: HIDE");
                }
                drawHeader(c, currentHeader);
                break;
            case MOVE_HEADER:
                if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
                    currentHeader = getHeaderViewForItem(topChildPosition, parent);
                    fixLayoutSize(parent, currentHeader);
                    moveHeader(c, currentHeader, childInContact);
                } else {
                    drawHeader(c, currentHeader);
                    if (wasPreviouslyHeader) {
                        mState = State.HIDE;
                        Log.d(TAG, "onDrawOver: STATE: MOVE HEADER");
                        Log.d(TAG, "onDrawOver: ASSIGNED STATE: HIDE");
                    } else {
                        mState = State.SCROLL;
                        Log.d(TAG, "onDrawOver: STATE: MOVE HEADER");
                        Log.d(TAG, "onDrawOver: ASSIGNED STATE: SCROLL");
                    }
                }
                break;
            case HIDE:
                if (mListener.isHeader(topChildPosition)) {
                    int bottom = topChild.getBottom();
                    headerCover = getHeaderViewForItem2(topChildPosition, parent);
                    fixLayoutSize2(parent, headerCover, bottom);
                    hideMovingHeader(c, headerCover, currentHeader);
                } else {
                    currentHeader = getHeaderViewForItem(parent.getChildAdapterPosition(childInContact), parent);
                    fixLayoutSize(parent, currentHeader);
                    if (wasPreviouslyHeader) {
                        mState = State.SCROLL;
                        wasPreviouslyHeader = false;
                        Log.d(TAG, "onDrawOver: ASSIGNED STATE: SCROLL");
                    } else {
                        mState = State.MOVE_HEADER;
                        Log.d(TAG, "onDrawOver: ASSIGNED STATE: MOVE HEADER");
                    }
                }
                drawHeader(c, currentHeader);
                break;
            default:
                break;
        }
    }

    private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
        int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
        int layoutResId = mListener.getHeaderLayout(headerPosition);
        View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        mListener.bindHeaderData(header, headerPosition);
        return header;
    }

    private View getHeaderViewForItem2(int itemPosition, RecyclerView parent) {
        int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
        int layoutResId = R.layout.item_obs_date_2;
        View header = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        mListener.bindHeaderData(header, headerPosition);
        return header;
    }

    private void drawHeader(Canvas c, View header) {
        c.save();
        c.translate(0, 0);
        header.draw(c);
        c.restore();
    }

    private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
        c.save();
        c.translate(0, nextHeader.getTop() - currentHeader.getHeight());
        currentHeader.draw(c);
        c.restore();
    }

    private void hideMovingHeader(Canvas c, View currentHeader, View nextHeader) {
        c.save();
        int d1 = currentHeader.getBottom() - nextHeader.getHeight();
        c.translate(0, d1);
        currentHeader.draw(c);
        c.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getBottom() > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    /**
     * Properly measures and layouts the top sticky header.
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private void fixLayoutSize(ViewGroup parent, View view) {

        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    private void fixLayoutSize2(ViewGroup parent, View view, int scrolledViewHeight) {

        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);

        view.layout(0, 0, view.getMeasuredWidth(), scrolledViewHeight);
    }

    public interface StickyHeaderInterface {

        /**
         * This method gets called by {@link HeaderItemDecoration} to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         *
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        int getHeaderPositionForItem(int itemPosition);

        /**
         * This method gets called by {@link HeaderItemDecoration} to get layout resource id for the header item at specified adapter's position.
         *
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        int getHeaderLayout(int headerPosition);

        /**
         * This method gets called by {@link HeaderItemDecoration} to setup the header View.
         *
         * @param header         View. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        void bindHeaderData(View header, int headerPosition);

        /**
         * This method gets called by {@link HeaderItemDecoration} to verify whether the item represents a header.
         *
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        boolean isHeader(int itemPosition);
    }
}
