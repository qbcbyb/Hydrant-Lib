package cn.qbcbyb.lib.views;

import android.content.Context;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Vikram on 01/04/2015.
 */
/* A view pool to manage more views than we can visibly handle */
public class ViewPool {

    /* An interface to the consumer of a view pool */
    public interface ViewPoolConsumer {
        public DeckChildView createView(Context context);

        public void prepareViewToEnterPool(DeckChildView v);

        public void prepareViewToLeavePool(DeckChildView v, int prepareData, boolean isNewView);

        public boolean hasPreferredData(DeckChildView v, int preferredData);
    }

    Context mContext;
    ViewPoolConsumer mViewCreator;
    LinkedList<DeckChildView> mPool = new LinkedList<DeckChildView>();

    /**
     * Initializes the pool with a fixed predetermined pool size
     */
    public ViewPool(Context context, ViewPoolConsumer viewCreator) {
        mContext = context;
        mViewCreator = viewCreator;
    }

    /**
     * Returns a view into the pool
     */
    void returnViewToPool(DeckChildView v) {
        mViewCreator.prepareViewToEnterPool(v);
        mPool.push(v);
    }

    /**
     * Gets a view from the pool and prepares it
     */
    DeckChildView pickUpViewFromPool(int preferredData, int prepareData) {
        DeckChildView v = null;
        boolean isNewView = false;
        if (mPool.isEmpty()) {
            v = mViewCreator.createView(mContext);
            isNewView = true;
        } else {
            // Try and find a preferred view
            Iterator<DeckChildView> iter = mPool.iterator();
            while (iter.hasNext()) {
                DeckChildView vpv = iter.next();
                if (mViewCreator.hasPreferredData(vpv, preferredData)) {
                    v = vpv;
                    iter.remove();
                    break;
                }
            }
            // Otherwise, just grab the first view
            if (v == null) {
                v = mPool.pop();
            }
        }
        mViewCreator.prepareViewToLeavePool(v, prepareData, isNewView);
        return v;
    }

    /**
     * Returns an iterator to the list of the views in the pool.
     */
    Iterator<DeckChildView> poolViewIterator() {
        if (mPool != null) {
            return mPool.iterator();
        }
        return null;
    }
}
