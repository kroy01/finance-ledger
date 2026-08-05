# Finance Ledger

Preliminary GNOME-native finance-ledger interface.

## Technology

- Java 25
- Maven
- Java-GI
- GTK 4
- libadwaita

## Current scope

- Native application window
- Sidebar navigation
- Placeholder pages
- Add Entry placeholder action
- No financial-domain implementation
- No persistent storage

## Main class

`com.roycorp.financeledger.FinanceLedgerApplication`

## Required JVM option

`--enable-native-access=ALL-UNNAMED`

## IntelliJ IDEA

Open `pom.xml` as a Maven project and configure:

- Project SDK: Java 25
- Maven home: Bundled Maven
- Maven importer JDK: Project SDK
- Maven runner JRE: Project SDK

For an IntelliJ Application run configuration, use:

- Main class: `com.roycorp.financeledger.FinanceLedgerApplication`
- VM options: `--enable-native-access=ALL-UNNAMED`

## License

Copyright 2026 Krishnendu Roy.

Licensed under the Apache License, Version 2.0. See `LICENSE` and
`NOTICE` for details.
