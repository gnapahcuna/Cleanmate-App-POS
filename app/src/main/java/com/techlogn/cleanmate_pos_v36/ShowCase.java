package com.techlogn.cleanmate_pos_v36;

import android.app.Activity;
import android.content.Context;

import ru.dimorinny.showcasecard.ShowCaseView;
import ru.dimorinny.showcasecard.position.Center;
import ru.dimorinny.showcasecard.position.ShowCasePosition;
import ru.dimorinny.showcasecard.position.ViewPosition;
import ru.dimorinny.showcasecard.radius.Radius;
import ru.dimorinny.showcasecard.radius.ShowCaseRadius;
import ru.dimorinny.showcasecard.step.ShowCaseStep;
import ru.dimorinny.showcasecard.step.ShowCaseStepDisplayer;

public class ShowCase {
    private Context mContext;

    public  ShowCase(Context context){
        mContext=context;
    }

    /*private void displayListOfSteps() {
        new ShowCaseStepDisplayer.Builder(mContext)
                .withScrollView(scrollView)
                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("This is the center of the screen. Tap anywhere to continue.")
                                .withTypedPosition(new Center())
                                .build(this)
                )
                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("This is the button you just clicked.")
                                .withTypedPosition(new ViewPosition(listOfSteps))
                                .build(this)
                )
                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("A dummy item to auto-scroll to.")
                                .withTypedPosition(new ViewPosition(dummyViewToScrollTo))
                                .build(this)
                )

                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("We end our showcase at the top button.")
                                .withTypedPosition(new ViewPosition(topLeft))
                                .build(this)
                )
                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("With custom radius")
                                .withTypedPosition(new ViewPosition(topLeftToolbar))
                                .withTypedRadius(new Radius(
                                        ViewUtils.convertDpToPx(
                                                this,
                                                100
                                        )
                                ))
                                .build(this)
                )
                .addStep(
                        new ShowCaseStep.Builder()
                                .withMessage("With custom color")
                                .withTypedPosition(new ViewPosition(topRightToolbar))
                                .withColor(R.color.colorPrimaryDark)
                                .build(this)
                )
                .build()
                .start();
    }*/
    public void showTipWithPosition(ShowCasePosition position,String message) {
        showTip(
                position,
                new Radius(190F),
                message
        );
    }

    private void showTip(ShowCasePosition position, ShowCaseRadius radius,String message) {
        new ShowCaseView.Builder(mContext)
                .withTypedPosition(position)
                .withTypedRadius(radius)
                .withContent(
                        message
                )
                .build()
                .show((Activity) mContext);
    }
}
