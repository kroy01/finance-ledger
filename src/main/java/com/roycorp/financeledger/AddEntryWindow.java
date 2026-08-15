package com.roycorp.financeledger;

import org.gnome.adw.Application;
import org.gnome.adw.ApplicationWindow;
import org.gnome.gtk.Window;
import org.gnome.adw.HeaderBar;
import org.gnome.gtk.ActionBar;
import org.gnome.adw.ToolbarView;
import org.gnome.gtk.Button;
import org.gnome.gtk.Align;
import org.gnome.gtk.Box;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.Stack;
import org.gnome.gtk.StackSwitcher;
import org.gnome.gtk.Label;
import org.gnome.gtk.CallbackAction;
import org.gnome.gtk.Shortcut;
import org.gnome.gtk.ShortcutController;
import org.gnome.gtk.ShortcutScope;
import org.gnome.gtk.ShortcutTrigger;

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
        toolbarView.addBottomBar(createFooter());

        setContent(toolbarView);

        installShortcuts();
    }
    private HeaderBar createHeaderBar() {
        return new HeaderBar();
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
    private ActionBar createFooter() {
        ActionBar footer = new ActionBar();

        footer.packStart(createShortcutHints());
        footer.packEnd(createFooterActions());

        return footer;
    }
    private Box createFooterActions() {
        Box actions = new Box(
                Orientation.HORIZONTAL,
                8
        );

        Button cancelButton =
                Button.withLabel("Cancel");

        cancelButton.onClicked(this::handleCancel);

        Button addButton =
                Button.withLabel("Add");

        addButton.addCssClass("suggested-action");
        addButton.onClicked(this::handleAdd);

        actions.append(cancelButton);
        actions.append(addButton);

        return actions;
    }
    private Box createShortcutHints() {
        Box hints = new Box(
                Orientation.HORIZONTAL,
                16
        );

        Label addHint = new Label("Ctrl+Enter  Add");

        addHint.addCssClass("dim-label");

        Label cancelHint = new Label("Esc  Cancel");

        cancelHint.addCssClass("dim-label");

        hints.append(addHint);
        hints.append(cancelHint);

        return hints;
    }
    private void handleAdd() {
        /*
         * Placeholder until entry submission exists.
         */
        close();
    }

    private void handleCancel() {
        close();
    }
    private void installShortcuts() {
        ShortcutController controller =
                new ShortcutController();

        controller.setScope(
                ShortcutScope.GLOBAL
        );

        controller.addShortcut(
                new Shortcut(
                        ShortcutTrigger.parseString("<Control>Return"),
                        new CallbackAction(
                                (widget, args) -> {
                                    handleAdd();
                                    return true;
                                }
                        )
                )
        );
        controller.addShortcut(
                new Shortcut(
                        ShortcutTrigger.parseString("<Control>KP_Enter"),
                        new CallbackAction(
                                (widget, args) -> {
                                    handleAdd();
                                    return true;
                                }
                        )
                )
        );
        controller.addShortcut(
                new Shortcut(
                        ShortcutTrigger.parseString("Escape"),
                        new CallbackAction(
                                (widget, args) -> {
                                    handleCancel();
                                    return true;
                                }
                        )
                )
        );

        addController(controller);
    }
}
