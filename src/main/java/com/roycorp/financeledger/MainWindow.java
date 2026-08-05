package com.roycorp.financeledger;

import org.gnome.adw.Application;
import org.gnome.adw.ApplicationWindow;
import org.gnome.adw.HeaderBar;
import org.gnome.adw.ToolbarView;
import org.gnome.gtk.Align;
import org.gnome.gtk.Box;
import org.gnome.gtk.Button;
import org.gnome.gtk.Image;
import org.gnome.gtk.Label;
import org.gnome.gtk.ListBox;
import org.gnome.gtk.ListBoxRow;
import org.gnome.gtk.MenuButton;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.PolicyType;
import org.gnome.gtk.Popover;
import org.gnome.gtk.ScrolledWindow;
import org.gnome.gtk.SelectionMode;
import org.gnome.gtk.Separator;
import org.gnome.gtk.Stack;

import java.util.HashMap;
import java.util.Map;

/**
 * Preliminary application shell.
 *
 * This class contains navigation and window structure only.
 * Finance logic, record counts and page contents are intentionally omitted.
 */
public final class MainWindow extends ApplicationWindow {

    private static final int SIDEBAR_WIDTH = 260;

    private final Application application;
    private final Stack pageStack;
    private final Map<ListBoxRow, String> rowTargets =
            new HashMap<>();

    public MainWindow(Application application) {
        super(application);

        this.application = application;

        setTitle("Finance Ledger");
        setDefaultSize(1200, 760);

        pageStack = createPageStack();
        setContent(createWindowLayout());
    }

    private Box createWindowLayout() {
        Box layout = new Box(
                Orientation.HORIZONTAL,
                0
        );

        layout.append(createSidebarPane());
        layout.append(new Separator(Orientation.VERTICAL));
        layout.append(createContentPane());

        return layout;
    }

    private ToolbarView createSidebarPane() {
        ToolbarView sidebarPane = new ToolbarView();
        sidebarPane.setSizeRequest(SIDEBAR_WIDTH, -1);
        sidebarPane.setHexpand(false);
        sidebarPane.setVexpand(true);

        HeaderBar sidebarHeader = new HeaderBar();

        /*
         * Window controls belong to the main header, not the sidebar header.
         */
        sidebarHeader.setShowStartTitleButtons(false);
        sidebarHeader.setShowEndTitleButtons(false);
        sidebarHeader.setShowTitle(false);
        sidebarHeader.packEnd(createSidebarMenuButton());

        sidebarPane.addTopBar(sidebarHeader);
        sidebarPane.setContent(createSidebarNavigation());

        return sidebarPane;
    }

    private ToolbarView createContentPane() {
        ToolbarView contentPane = new ToolbarView();
        contentPane.setHexpand(true);
        contentPane.setVexpand(true);

        HeaderBar contentHeader = new HeaderBar();

        Button addEntryButton =
                Button.withLabel("Add Entry");

        addEntryButton.addCssClass("suggested-action");
        addEntryButton.setTooltipText("Add a finance entry");

        /*
         * Placeholder behaviour until the Add Entry window is implemented.
         */
        addEntryButton.onClicked(
                () -> pageStack.setVisibleChildName("ledger")
        );

        contentHeader.packEnd(addEntryButton);

        contentPane.addTopBar(contentHeader);
        contentPane.setContent(pageStack);

        return contentPane;
    }

    private MenuButton createSidebarMenuButton() {
        MenuButton menuButton = new MenuButton();

        menuButton.setIconName("open-menu-symbolic");
        menuButton.setTooltipText("Application menu");
        menuButton.setPrimary(true);

        Popover popover = new Popover();

        Box menuContent = new Box(
                Orientation.VERTICAL,
                2
        );

        menuContent.setMarginStart(6);
        menuContent.setMarginEnd(6);
        menuContent.setMarginTop(6);
        menuContent.setMarginBottom(6);
        menuContent.setSizeRequest(190, -1);

        Button newTabButton = createMenuOption(
                "tab-new-symbolic",
                "New Tab"
        );

        Button newWindowButton = createMenuOption(
                "window-new-symbolic",
                "New Window"
        );

        /*
         * New Tab is currently a structural placeholder.
         */
        newTabButton.onClicked(menuButton::popdown);

        /*
         * New Window already has lightweight window-shell behaviour.
         * It creates another empty application window with the same structure.
         */
        newWindowButton.onClicked(() -> {
            menuButton.popdown();

            MainWindow newWindow =
                    new MainWindow(application);

            newWindow.present();
        });

        menuContent.append(newTabButton);
        menuContent.append(newWindowButton);

        popover.setChild(menuContent);
        menuButton.setPopover(popover);

        return menuButton;
    }

    private static Button createMenuOption(
            String iconName,
            String text
    ) {
        Button button = new Button();

        button.addCssClass("flat");
        button.setHalign(Align.FILL);
        button.setHexpand(true);

        Box content = new Box(
                Orientation.HORIZONTAL,
                10
        );

        content.setMarginStart(8);
        content.setMarginEnd(8);
        content.setMarginTop(6);
        content.setMarginBottom(6);

        Image icon = Image.fromIconName(iconName);
        icon.setPixelSize(16);

        Label label = new Label(text);
        label.setXalign(0.0f);
        label.setHexpand(true);

        content.append(icon);
        content.append(label);

        button.setChild(content);

        return button;
    }

    private ScrolledWindow createSidebarNavigation() {
        ListBox navigation = new ListBox();

        navigation.addCssClass("navigation-sidebar");
        navigation.setSelectionMode(SelectionMode.SINGLE);
        navigation.setActivateOnSingleClick(true);

        navigation.setMarginStart(8);
        navigation.setMarginEnd(8);
        navigation.setMarginTop(8);
        navigation.setMarginBottom(8);

        ListBoxRow overviewRow = addNavigationRow(
                navigation,
                "view-grid-symbolic",
                "Overview",
                "overview"
        );

        addNavigationRow(
                navigation,
                "view-list-symbolic",
                "Cash flow",
                "cash-flow"
        );

        addNavigationRow(
                navigation,
                "x-office-calendar-symbolic",
                "This month",
                "this-month"
        );

        addSectionHeader(
                navigation,
                "BREAKDOWN"
        );

        addNavigationRow(
                navigation,
                "go-down-symbolic",
                "Expenses",
                "expenses"
        );

        addNavigationRow(
                navigation,
                "go-up-symbolic",
                "Income",
                "income"
        );

        addNavigationRow(
                navigation,
                "view-refresh-symbolic",
                "Recurring",
                "recurring"
        );

        addNavigationRow(
                navigation,
                "system-users-symbolic",
                "People",
                "people"
        );

        addSectionHeader(
                navigation,
                "ANALYSIS"
        );

        addNavigationRow(
                navigation,
                "folder-symbolic",
                "Investments",
                "investments"
        );

        addNavigationRow(
                navigation,
                "office-chart-bar-symbolic",
                "Insights",
                "insights"
        );

        addNavigationRow(
                navigation,
                "accessories-calculator-symbolic",
                "Tax",
                "tax"
        );

        addSectionHeader(
                navigation,
                "PLAN & ENTRY"
        );

        addNavigationRow(
                navigation,
                "document-edit-symbolic",
                "Planner",
                "planner"
        );

        addNavigationRow(
                navigation,
                "view-list-symbolic",
                "Monthly review",
                "monthly-review"
        );

        navigation.onRowSelected(row -> {
            if (row == null) {
                return;
            }

            String pageName = rowTargets.get(row);

            if (pageName != null) {
                pageStack.setVisibleChildName(pageName);
            }
        });

        navigation.selectRow(overviewRow);

        ScrolledWindow scrolledWindow =
                new ScrolledWindow();

        scrolledWindow.setPolicy(
                PolicyType.NEVER,
                PolicyType.AUTOMATIC
        );

        scrolledWindow.setVexpand(true);
        scrolledWindow.setChild(navigation);

        return scrolledWindow;
    }

    private ListBoxRow addNavigationRow(
            ListBox navigation,
            String iconName,
            String title,
            String pageName
    ) {
        ListBoxRow row = new ListBoxRow();

        row.setSelectable(true);
        row.setActivatable(true);

        Box rowContent = new Box(
                Orientation.HORIZONTAL,
                12
        );

        rowContent.setMarginStart(10);
        rowContent.setMarginEnd(10);
        rowContent.setMarginTop(8);
        rowContent.setMarginBottom(8);

        Image icon = Image.fromIconName(iconName);
        icon.setPixelSize(16);

        Label titleLabel = new Label(title);
        titleLabel.setXalign(0.0f);
        titleLabel.setHexpand(true);

        rowContent.append(icon);
        rowContent.append(titleLabel);

        row.setChild(rowContent);
        navigation.append(row);
        rowTargets.put(row, pageName);

        return row;
    }

    private static void addSectionHeader(
            ListBox navigation,
            String title
    ) {
        ListBoxRow headerRow = new ListBoxRow();

        headerRow.setSelectable(false);
        headerRow.setActivatable(false);

        Label headerLabel = new Label(title);

        headerLabel.setXalign(0.0f);
        headerLabel.addCssClass("caption");
        headerLabel.addCssClass("dim-label");

        headerLabel.setMarginStart(10);
        headerLabel.setMarginEnd(10);
        headerLabel.setMarginTop(14);
        headerLabel.setMarginBottom(4);

        headerRow.setChild(headerLabel);
        navigation.append(headerRow);
    }

    private Stack createPageStack() {
        Stack stack = new Stack();

        addEmptyPage(stack, "overview", "Overview");
        addEmptyPage(stack, "cash-flow", "Cash flow");
        addEmptyPage(stack, "this-month", "This month");

        addEmptyPage(stack, "expenses", "Expenses");
        addEmptyPage(stack, "income", "Income");
        addEmptyPage(stack, "recurring", "Recurring");
        addEmptyPage(stack, "people", "People");

        addEmptyPage(stack, "investments", "Investments");
        addEmptyPage(stack, "insights", "Insights");
        addEmptyPage(stack, "tax", "Tax");

        addEmptyPage(stack, "planner", "Planner");

        addEmptyPage(
                stack,
                "monthly-review",
                "Monthly review"
        );

        stack.setVisibleChildName("overview");

        return stack;
    }

    private static void addEmptyPage(
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
}
