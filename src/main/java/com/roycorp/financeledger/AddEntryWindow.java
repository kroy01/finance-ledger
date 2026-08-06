package com.roycorp.financeledger;

import org.gnome.adw.Application;
import org.gnome.adw.ApplicationWindow;
import org.gnome.gtk.Window;
import org.gnome.adw.HeaderBar;
import org.gnome.adw.ToolbarView;
import org.gnome.gtk.Button;
import org.gnome.gtk.Align;
import org.gnome.gtk.Box;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.Stack;
import org.gnome.gtk.StackSwitcher;

public final class AddEntryWindow
        extends ApplicationWindow {

    public AddEntryWindow(
            Application application,
            Window parent
    ) {
        super(application);

        setTitle("Add Entry");
        setDefaultSize(720, 560);

        setTransientFor(parent);
        setModal(true);
        setDestroyWithParent(true);

        Stack entryStack = createEntryStack();

        ToolbarView toolbarView = new ToolbarView();
        toolbarView.addTopBar(createHeaderBar());
        toolbarView.setContent(
                createContent(entryStack)
        );

        setContent(toolbarView);
    }
    private HeaderBar createHeaderBar() {
        HeaderBar headerBar = new HeaderBar();

        Button cancelButton = Button.withLabel("Cancel");

        cancelButton.onClicked(this::close);
        headerBar.packStart(cancelButton);

        Button addButton = Button.withLabel("Add");

        addButton.addCssClass("suggested-action");
        addButton.onClicked(this::close);

        headerBar.packEnd(addButton);

        return headerBar;
    }
    private Stack createEntryStack() {
        Stack stack = new Stack();

        addEmptyEntryPage(
                stack,
                "income",
                "Income"
        );

        addEmptyEntryPage(
                stack,
                "expense",
                "Expense"
        );

        addEmptyEntryPage(
                stack,
                "investment",
                "Investment"
        );

        stack.setVisibleChildName("expense");

        return stack;
    }
    private static void addEmptyEntryPage(
            Stack stack,
            String pageName,
            String title
    ) {
        Box emptyPage = new Box(
                Orientation.VERTICAL,
                0
        );

        emptyPage.setHexpand(true);
        emptyPage.setVexpand(true);

        stack.addTitled(
                emptyPage,
                pageName,
                title
        );
    }
    private Box createContent(Stack entryStack) {
        Box content = new Box(
                Orientation.VERTICAL,
                18
        );

        content.setMarginStart(24);
        content.setMarginEnd(24);
        content.setMarginTop(24);
        content.setMarginBottom(24);

        StackSwitcher entryTypeSwitcher =
                new StackSwitcher();

        entryTypeSwitcher.setStack(entryStack);
        entryTypeSwitcher.setHalign(Align.CENTER);

        entryStack.setHexpand(true);
        entryStack.setVexpand(true);

        content.append(entryTypeSwitcher);
        content.append(entryStack);

        return content;
    }
}