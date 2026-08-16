package com.roycorp.financeledger.ui.entry;

import org.gnome.gtk.Align;
import org.gnome.gtk.Box;
import org.gnome.gtk.Button;
import org.gnome.gtk.DropDown;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Frame;
import org.gnome.gtk.Label;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.PolicyType;
import org.gnome.gtk.ScrolledWindow;
import org.gnome.gtk.Separator;
import org.gnome.gtk.Stack;
import org.gnome.gtk.StackSwitcher;
import org.gnome.gtk.Switch;
import org.gnome.gtk.CssProvider;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.StyleContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class ExpenseEntryView
        extends ScrolledWindow {
    private static final String[] CURRENCIES = {
            "₹ INR",
            "$ USD",
            "€ EUR",
            "£ GBP",
            "¥ JPY"
    };

    private static final String[] CURRENCY_SYMBOLS = {
            "₹",
            "$",
            "€",
            "£",
            "¥"
    };

    public ExpenseEntryView() {
        installStyles();
        Box form = new Box(
                Orientation.VERTICAL,
                24
        );

        form.setMarginStart(12);
        form.setMarginEnd(12);
        form.setMarginTop(12);
        form.setMarginBottom(24);

        form.append(createAmountAndDateSection());
        form.append(createCategorySection());
        form.append(createPaidToSection());
        form.append(createPaymentModeSection());
        form.append(createRecurringSection());
        form.append(createSummarySection());

        setChild(form);

        setHexpand(true);
        setVexpand(true);

        setPolicy(
                PolicyType.NEVER,
                PolicyType.AUTOMATIC
        );
    }

    private Box createAmountAndDateSection() {
        Box section = createSection("Amount");

        Box row = new Box(
                Orientation.HORIZONTAL,
                8
        );

        /*
         * Visual amount field:
         *
         * ┌─────────────────────┐
         * │ ₹  7,000            │
         * └─────────────────────┘
         *
         * The currency symbol is not part of the
         * editable amount text.
         */
        Box amountField = new Box(
                Orientation.HORIZONTAL,
                6
        );

        amountField.addCssClass("amount-field");
        amountField.setHexpand(true);

        Label currencySymbol =
                new Label(CURRENCY_SYMBOLS[0]);

        currencySymbol.addCssClass(
                "amount-currency-symbol"
        );

        Entry amountEntry = new Entry();

        amountEntry.setPlaceholderText("Amount");
        amountEntry.setHexpand(true);

        /*
         * The outer amountField draws the visual border,
         * so the Entry itself should not draw another one.
         */
        amountEntry.setHasFrame(false);
        amountEntry.addCssClass("flat");

        amountField.append(currencySymbol);
        amountField.append(amountEntry);

        DropDown currencySelector =
                DropDown.fromStrings(
                        CURRENCIES
                );

        /*
         * Keep the symbol at the beginning of the amount
         * field synchronized with the selected currency.
         */
        currencySelector.onNotify(
                "selected",
                _ -> {
                    int selected =
                            currencySelector.getSelected();

                    if (selected >= 0
                            && selected
                            < CURRENCY_SYMBOLS.length) {

                        currencySymbol.setLabel(
                                CURRENCY_SYMBOLS[selected]
                        );
                    }
                }
        );

        LocalDate currentDate =
                LocalDate.now();

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy"
                );

        Button dateButton =
                Button.withLabel(
                        currentDate.format(
                                dateFormatter
                        )
                );

        /*
         * Calendar popup behaviour is intentionally
         * deferred to the dedicated date-picker issue.
         */

        row.append(amountField);
        row.append(currencySelector);
        row.append(dateButton);

        section.append(row);

        return section;
    }
    private static Button createAddNewButton(
            String label
    ) {
        Button button =
                Button.withLabel("+ " + label);

        button.setHalign(Align.START);
        button.addCssClass("add-new-action");

        return button;
    }
    private void installStyles() {
        CssProvider provider =
                new CssProvider();

        provider.loadFromString("""
            /*
             * ---------------------------------------------------------
             * Add-new actions
             * ---------------------------------------------------------
             */

            .add-new-action {
                background-color: rgba(
                    53,
                    132,
                    228,
                    0.14
                );

                color: rgb(
                    53,
                    132,
                    228
                );

                border-style: dashed;
                border-width: 1px;

                border-color: rgba(
                    53,
                    132,
                    228,
                    0.70
                );

                border-radius: 9999px;

                padding: 6px 12px;
            }

            .add-new-action:hover {
                background-color: rgba(
                    53,
                    132,
                    228,
                    0.22
                );
            }

            .add-new-action:active {
                background-color: rgba(
                    53,
                    132,
                    228,
                    0.30
                );
            }


            /*
             * ---------------------------------------------------------
             * Amount field
             * ---------------------------------------------------------
             */

            .amount-field {
                padding: 0 10px;

                border-radius: 8px;

                border-width: 1px;
                border-style: solid;

                border-color: rgba(
                    128,
                    128,
                    128,
                    0.35
                );
            }

            .amount-currency-symbol {
                opacity: 0.75;
                font-weight: 600;
                margin-right: 2px;
            }


            /*
             * ---------------------------------------------------------
             * Recurring-cost card
             * ---------------------------------------------------------
             */

            .recurring-card {
                border-style: solid;
                border-width: 1px;

                border-color: rgba(
                    128,
                    128,
                    128,
                    0.28
                );

                border-radius: 12px;
            }

            .recurring-title {
                font-weight: 600;
            }


            /*
             * ---------------------------------------------------------
             * Segmented switchers
             *
             * Used by:
             * - Income / Expense / Investment
             * - Merchant / People
             * ---------------------------------------------------------
             */

            .segmented-switcher {
                background-color: rgba(
                    128,
                    128,
                    128,
                    0.18
                );

                border-radius: 11px;

                padding: 3px;
            }


            /*
             * Base appearance for every segment.
             *
             * The margin leaves visible space between the selected
             * segment and the outer segmented container, giving the
             * selected sub-box the smaller "floating" appearance.
             */

            .segmented-switcher button {
                background-image: none;
                background-color: transparent;

                border-style: none;
                border-width: 0;

                border-radius: 8px;

                box-shadow: none;

                min-height: 24px;

                margin: 1px;

                padding-top: 2px;
                padding-bottom: 2px;
                padding-left: 14px;
                padding-right: 14px;

                opacity: 0.68;
            }


            /*
             * Inactive item hover.
             */

            .segmented-switcher button:not(:checked):hover {
                background-color: rgba(
                    128,
                    128,
                    128,
                    0.12
                );

                opacity: 0.88;
            }


            /*
             * Selected segment.
             *
             * Explicit accent colouring is used instead of depending
             * on a theme variable, so selection remains visually clear
             * in both light and dark appearance.
             */

            .segmented-switcher button:checked {
                background-image: none;

                background-color: rgba(
                    53,
                    132,
                    228,
                    0.24
                );

                color: rgb(
                    53,
                    132,
                    228
                );

                border-style: solid;
                border-width: 1px;

                border-color: rgba(
                    53,
                    132,
                    228,
                    0.38
                );

                opacity: 1;

                box-shadow:
                    0 1px 2px
                    rgba(
                        0,
                        0,
                        0,
                        0.14
                    );
            }


            /*
             * Selected item hover.
             */

            .segmented-switcher button:checked:hover {
                background-color: rgba(
                    53,
                    132,
                    228,
                    0.31
                );

                border-color: rgba(
                    53,
                    132,
                    228,
                    0.48
                );
            }


            /*
             * ---------------------------------------------------------
             * Main entry-type switcher
             * Income / Expense / Investment
             * ---------------------------------------------------------
             */

            .entry-type-switcher {
                min-height: 32px;
            }

            .entry-type-switcher button {
                min-height: 24px;

                padding-left: 22px;
                padding-right: 22px;

                font-weight: 600;
            }


            /*
             * ---------------------------------------------------------
             * Paid-to switcher
             * Merchant / People
             * ---------------------------------------------------------
             */

            .paid-to-switcher {
                min-height: 28px;

                border-radius: 9px;

                padding: 2px;
            }

            .paid-to-switcher button {
                min-height: 22px;

                border-radius: 7px;

                margin: 1px;

                padding-top: 1px;
                padding-bottom: 1px;
                padding-left: 12px;
                padding-right: 12px;
            }
            """);

        StyleContext.addProviderForDisplay(
                getDisplay(),
                provider,
                Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION
        );
    }

    private Box createCategorySection() {
        Box section = createSection("Category");

        Button newCategoryButton =
                createAddNewButton("New Category");

        /*
         * Opening the category child window is
         * intentionally not implemented here.
         */

        section.append(newCategoryButton);

        return section;
    }

    private Box createPaidToSection() {
        Box section = new Box(
                Orientation.VERTICAL,
                8
        );

        Stack paidToStack =
                new Stack();

        paidToStack.addTitled(
                createMerchantPage(),
                "merchant",
                "Merchant"
        );

        paidToStack.addTitled(
                createPeoplePage(),
                "people",
                "People"
        );

        paidToStack.setVisibleChildName(
                "merchant"
        );

        StackSwitcher paidToSwitcher =
                new StackSwitcher();

        paidToSwitcher.setStack(
                paidToStack
        );

        paidToSwitcher.addCssClass(
                "segmented-switcher"
        );

        paidToSwitcher.addCssClass(
                "paid-to-switcher"
        );

        paidToSwitcher.setHalign(
                Align.START
        );

        paidToSwitcher.setValign(
                Align.CENTER
        );

        /*
         * Heading and Merchant/People selector now form
         * one horizontal header row.
         */
        Box header = new Box(
                Orientation.HORIZONTAL,
                12
        );

        Label heading =
                new Label("Paid to");

        heading.setHalign(Align.START);
        heading.setValign(Align.CENTER);
        heading.addCssClass("heading");

        paidToSwitcher.setHalign(Align.START);
        paidToSwitcher.setValign(Align.CENTER);

        header.append(heading);
        header.append(paidToSwitcher);

        section.append(header);
        section.append(paidToStack);

        return section;
    }

    private Box createMerchantPage() {
        Box page = new Box(
                Orientation.VERTICAL,
                8
        );

        /*
         * Merchant creation is intentionally deferred
         * to the child-window issue.
         */

        page.append(
                createAddNewButton(
                        "New Merchant"
                )
        );

        return page;
    }

    private Box createPeoplePage() {
        Box page = new Box(
                Orientation.VERTICAL,
                8
        );

        /*
         * Person creation is intentionally deferred
         * to the child-window issue.
         */

        page.append(
                createAddNewButton(
                        "New Person"
                )
        );

        return page;
    }

    private Box createPaymentModeSection() {
        Box section =
                createSection("Payment mode");

        DropDown paymentModeSelector =
                DropDown.fromStrings(
                        new String[]{
                                "Cash",
                                "Card",
                                "UPI",
                                "Bank transfer"
                        }
                );

        paymentModeSelector.setHalign(Align.START);

        section.append(paymentModeSelector);

        return section;
    }

    private Box createRecurringSection() {
        Box section = new Box(
                Orientation.VERTICAL,
                0
        );

        Frame recurringFrame =
                new Frame((String) null);

        recurringFrame.addCssClass(
                "recurring-card"
        );

        Box cardContent = new Box(
                Orientation.VERTICAL,
                0
        );

        Box header = new Box(
                Orientation.HORIZONTAL,
                12
        );

        header.setMarginStart(16);
        header.setMarginEnd(16);
        header.setMarginTop(12);
        header.setMarginBottom(12);

        Switch recurringSwitch =
                new Switch();

        recurringSwitch.setValign(
                Align.CENTER
        );

        Box textBlock = new Box(
                Orientation.VERTICAL,
                2
        );

        textBlock.setHexpand(true);

        Label title =
                new Label("Recurring cost");

        title.setHalign(Align.START);
        title.addCssClass(
                "recurring-title"
        );

        Label description =
                new Label(
                        "Turn on for anything that repeats — " +
                                "a subscription, a rent, or a fixed transfer plan."
                );

        description.setHalign(Align.START);
        description.setWrap(true);
        description.addCssClass(
                "dim-label"
        );

        textBlock.append(title);
        textBlock.append(description);

        header.append(recurringSwitch);
        header.append(textBlock);

        cardContent.append(header);

        /*
         * Recurring-detail content will later be appended
         * below this header in Issue #11.
         *
         * For Issue #8 the switch is intentionally not
         * connected to any expanding content.
         */

        recurringFrame.setChild(
                cardContent
        );

        section.append(recurringFrame);

        return section;
    }
    private Box createSummarySection() {
        Box section =
                createSection(
                        "Will be recorded as"
                );

        Frame summaryFrame =
                new Frame((String) null);

        Box table = new Box(
                Orientation.VERTICAL,
                0
        );

        appendSummaryRow(
                table,
                "Type",
                "Expense",
                false
        );

        appendSummaryRow(
                table,
                "Amount",
                "—",
                true
        );

        appendSummaryRow(
                table,
                "Date",
                "—",
                true
        );

        appendSummaryRow(
                table,
                "Category",
                "—",
                true
        );

        appendSummaryRow(
                table,
                "Paid to",
                "—",
                true
        );

        appendSummaryRow(
                table,
                "Payment mode",
                "—",
                true
        );

        appendSummaryRow(
                table,
                "Recurring",
                "—",
                true
        );

        summaryFrame.setChild(table);

        section.append(summaryFrame);

        return section;
    }

    private static void appendSummaryRow(
            Box table,
            String name,
            String value,
            boolean separatorBefore
    ) {
        if (separatorBefore) {
            table.append(
                    new Separator(
                            Orientation.HORIZONTAL
                    )
            );
        }

        Box row = createSummaryRow(
                name,
                value
        );

        row.setMarginStart(12);
        row.setMarginEnd(12);
        row.setMarginTop(10);
        row.setMarginBottom(10);

        table.append(row);
    }

    private static Box createSection(
            String title
    ) {
        Box section = new Box(
                Orientation.VERTICAL,
                8
        );

        Label heading = new Label(title);

        heading.setHalign(Align.START);
        heading.addCssClass("heading");

        section.append(heading);

        return section;
    }

    private static Box createSummaryRow(
            String name,
            String value
    ) {
        Box row = new Box(
                Orientation.HORIZONTAL,
                12
        );

        Label nameLabel =
                new Label(name);

        nameLabel.setHalign(Align.START);
        nameLabel.setHexpand(true);

        Label valueLabel =
                new Label(value);

        valueLabel.setHalign(Align.END);
        valueLabel.addCssClass("dim-label");

        row.append(nameLabel);
        row.append(valueLabel);

        return row;
    }
}