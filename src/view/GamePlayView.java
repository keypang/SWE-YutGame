package view;

import model.YutResult;

public interface GamePlayView {

    interface ThrowButtonListener {
        YutResult onThrowButtonClicked();
    }

    void setThrowButtonListener(ThrowButtonListener throwButtonListener);

}