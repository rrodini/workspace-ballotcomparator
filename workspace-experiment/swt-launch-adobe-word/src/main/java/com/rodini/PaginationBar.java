package com.rodini;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PaginationBar extends Composite {

    private int currentPage = 1;
    private int totalPages = 1;

    private Button btnFirst;
    private Button btnPrev;
    private Button btnNext;
    private Button btnLast;
    private Label lblPageInfo;

    private Consumer<Integer> onPageChange = page -> {};

    public PaginationBar(Composite parent, int style) {
        super(parent, style);
        createControls();
        updateControls();
    }

    private void createControls() {

        // Outer layout (1 column)
        GridLayout outerLayout = new GridLayout(1, false);
        outerLayout.marginWidth = 0;
        outerLayout.marginHeight = 0;
        setLayout(outerLayout);

        // Inner composite that will be centered
        Composite inner = new Composite(this, SWT.NONE);
        GridData innerData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        inner.setLayoutData(innerData);

        GridLayout layout = new GridLayout(5, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 12;
        inner.setLayout(layout);

        btnFirst = new Button(inner, SWT.PUSH);
        btnFirst.setText("⏮ First");
        btnFirst.addListener(SWT.Selection, e -> goToPage(1));

        btnPrev = new Button(inner, SWT.PUSH);
        btnPrev.setText("◀ Prev");
        btnPrev.addListener(SWT.Selection, e -> goToPage(currentPage - 1));

        lblPageInfo = new Label(inner, SWT.NONE);
        lblPageInfo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

        btnNext = new Button(inner, SWT.PUSH);
        btnNext.setText("Next ▶");
        btnNext.addListener(SWT.Selection, e -> goToPage(currentPage + 1));

        btnLast = new Button(inner, SWT.PUSH);
        btnLast.setText("Last ⏭");
        btnLast.addListener(SWT.Selection, e -> goToPage(totalPages));
    }

    private void updateControls() {
        lblPageInfo.setText("Page " + currentPage + " of " + totalPages);

        btnFirst.setEnabled(currentPage > 1);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
        btnLast.setEnabled(currentPage < totalPages);

        layout(true, true);
    }

    private void goToPage(int page) {
        int newPage = Math.max(1, Math.min(page, totalPages));
        if (newPage != currentPage) {
            currentPage = newPage;
            updateControls();
            onPageChange.accept(currentPage);
        }
    }

    // ---------------------------
    // Public API
    // ---------------------------

    public void setTotalPages(int totalPages) {
        this.totalPages = Math.max(1, totalPages);
        if (currentPage > this.totalPages) {
            currentPage = this.totalPages;
        }
        updateControls();
    }

    public void setCurrentPage(int page) {
        goToPage(page);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setOnPageChange(Consumer<Integer> listener) {
        this.onPageChange = listener != null ? listener : page -> {};
    }
}