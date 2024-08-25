# FinancePulse

FinancePulse is a decision support system to meet the needs of performance analysis in terms of credit, expenditure, revenue, and financing. 
This system aggregates the different data and displays them in the form of graphs, tables, or other visual representations. 
In addition, we have integrated prediction functionalities to estimate the future state of the treasury, using data mining techniques. 
Ultimately, we would like to provide proactive liquidity management by offering a quick and in-depth understanding of the financial situation, presented in an interactive way.
# Installation

    1.Clone the repository.
    2. Download and install Node.js.
    3. Download and install Oracle 19C EE.
    4.Download and install Pyhton.
    5. Run the command npm install.

# Usage

FinancePulse is a confidential system developed for decision-makers in the Algerian Ministry of Finance, and only system actors can use it. There are six roles:

# 1-Back Office User:

.Access the system after authentication.
.Process wilaya treasury data, which involves extracting data from the treasury database of accounting posts in various forms (text documents, manually entered databases) and transmitting it through the ETL process (Extraction, Transformation, Loading into our warehouse).
.Communicate additions to the Front Office Administrator and be notified of their feedback.
# 2-Front Office Administrator:

.Access the system after authentication.
.Manage users, including adding, deleting, or modifying user roles.
.View the dashboard of revenues, expenses, funding, and credits of the 61 accounting posts.
.In parallel, there is a prediction system that offers forecasts of future expenses, credits, revenues, and funding.
.Communicate decisions to the Back Office Administrator and be notified of their additions.
# 3-High-Level User:

.Access the system after authentication.
.View the dashboard of revenues, expenses, credits, and debts of the 61 accounting posts.
.In parallel, there is a prediction system that offers forecasts of future expenses, credits, revenues, and funding.
.Inform the Expenditure and Financing User of their decisions.
# 4-Expenditure User:

.Access the system after authentication.
.View the dashboard of expenses and credits of the 61 accounting posts.
.In parallel, there is a prediction system that offers forecasts of future expenses.
.Receive decisions made by the High-Level User concerning their division.
# 5-Revenue User:

.Access the system after authentication.
.View the dashboard of revenues of the 61 accounting posts.
.In parallel, there is a prediction system that offers forecasts of future revenues.
# 6-Financing User:

.Access the system after authentication.
.View the dashboard related to state debt market data.
.In parallel, there is a prediction system that offers forecasts of future funding.
.Receive decisions made by the High-Level User concerning their division.

# License

This project is licensed under the [MIT License]. See the LICENSE.md file for more details.
Authors

    -Aymen Khoukhi (the sole author for now)

# Project Status

    -Stable
