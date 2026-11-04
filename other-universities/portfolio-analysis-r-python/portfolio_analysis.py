"""
Χρηματοοικονομικά Υπολογιστικά Πακέτα - Ανάλυση Χαρτοφυλακίου Μετοχών
Ομάδα Μετοχών: AMD, AXON, GME, PFE

Αυτό το script κάνει ΤΗΝ ΙΔΙΑ δουλειά με το portfolio_analysis.R (ίδιοι
τύποι, ίδιες περίοδοι, ίδια λογική), αλλά σε Python (pandas/numpy) αντί
για R. Χρησιμοποιήθηκε για να παραχθούν/επαληθευτούν όλοι οι αριθμοί
που εμφανίζονται στο Portfolio_Analysis_Report.docx.

ΠΡΟΑΠΑΙΤΟΥΜΕΝΑ:
  pip install pandas numpy

ΠΩΣ ΤΡΕΧΕΙ:
  1. Βάλε αυτό το .py αρχείο στη ρίζα του project (δίπλα στον φάκελο "data").
  2. Τρέξε: python portfolio_analysis.py
  3. Τα αποτελέσματα τυπώνονται στο τερματικό/κονσόλα, τμήμα-τμήμα (1 έως 6).
"""

import pandas as pd
import numpy as np
import os

pd.set_option('display.width', 140)

# ----------------------------------------------------------------------
# Φάκελος με τα δεδομένα - εντοπίζεται αυτόματα σε σχέση με το πού
# βρίσκεται αυτό το .py αρχείο.
# ----------------------------------------------------------------------
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))


def find_data_dir():
    candidates = [
        os.path.join(SCRIPT_DIR, "data"),
        SCRIPT_DIR,
    ]
    for c in candidates:
        if os.path.isfile(os.path.join(c, "AMD.csv")):
            return c
    raise FileNotFoundError(
        "Δεν βρέθηκαν τα csv αρχεία (AMD.csv κ.λπ.). Βάλε το .py στη ρίζα "
        "του project, δίπλα στον φάκελο 'data'."
    )


DATA_DIR = find_data_dir()

TICKERS = ["AMD", "AXON", "GME", "PFE"]

# Ποσά επένδυσης ανά τίτλο
INVESTMENT = {"AMD": 4000, "AXON": 3000, "GME": 2000, "PFE": 2000}

# Ορισμός περιόδων (βάσει των πραγματικών ημερομηνιών των δεδομένων: 31/12/2010 - 30/12/2013)
#   Έτος 1: 31/12/2010 - 30/12/2011
#   Έτος 2: 03/01/2012 - 31/12/2012
#   Έτος 3: 02/01/2013 - 30/12/2013
START_INVEST = "2012-01-03"   # αρχή Έτους 2 -> αρχή περιόδου επένδυσης
END_INVEST   = "2013-12-30"   # τέλος Έτους 3 -> τέλος περιόδου επένδυσης
START_YEAR3  = "2013-01-02"   # αρχή Έτους 3 -> για SMA & RSI (διάστημα 1 έτους)


def load(ticker):
    """Διαβάζει ένα csv Yahoo Finance και το μετατρέπει σε DataFrame
    ταξινομημένο χρονολογικά, με τη Ημερομηνία ως index (αντίστοιχο του
    xts στο R)."""
    path = os.path.join(DATA_DIR, f"{ticker}.csv")
    df = pd.read_csv(path)
    df['Date'] = pd.to_datetime(df['Date'])
    df = df.sort_values('Date').reset_index(drop=True)
    df = df.set_index('Date')
    df.columns = [f"{ticker}.Open", f"{ticker}.High", f"{ticker}.Low",
                  f"{ticker}.Close", f"{ticker}.Adjusted", f"{ticker}.Volume"]
    return df


data = {t: load(t) for t in TICKERS}

# ========================================================================
# 1) ΕΛΕΓΧΟΣ ΔΕΔΟΜΕΝΩΝ (3 τρόποι)
# ========================================================================
print("=" * 70)
print("1) ΕΛΕΓΧΟΣ ΔΕΔΟΜΕΝΩΝ")
print("=" * 70)
for t in TICKERS:
    d = data[t]
    print(f"\n--- {t} ---")
    print("Διάσταση (γραμμές, στήλες):", d.shape)
    print("Εύρος ημερομηνιών:", d.index.min().date(), "έως", d.index.max().date())
    print("Πλήθος ελλειπουσών τιμών (NA):", int(d.isna().sum().sum()))
    print("Ελάχιστη τιμή Close:", round(d[f"{t}.Close"].min(), 2))
    print("Μέγιστη τιμή Close:", round(d[f"{t}.Close"].max(), 2))
    # Τρόπος 1 (οπτικός έλεγχος): ξεσχολίασε τις 2 παρακάτω γραμμές αν
    # τρέχεις σε Jupyter/interactive περιβάλλον για να δεις το γράφημα.
    # import matplotlib.pyplot as plt
    # d[f"{t}.Close"].plot(title=f"{t} - Τιμή Κλεισίματος (έλεγχος δεδομένων)"); plt.show()

# ========================================================================
# 2) ΣΥΝΟΛΙΚΕΣ ΑΠΟΔΟΣΕΙΣ (Έτος 2 - Έτος 3)
# ========================================================================
print("\n" + "=" * 70)
print("2) ΣΥΝΟΛΙΚΕΣ ΑΠΟΔΟΣΕΙΣ (Έτος 2 - Έτος 3)")
print("=" * 70)

multi = pd.concat([data[t][f"{t}.Adjusted"] for t in TICKERS], axis=1)
multi.columns = TICKERS
multi_period = multi.loc[START_INVEST:END_INVEST]

first_row = multi_period.iloc[0]
last_row = multi_period.iloc[-1]
print(f"Πρώτη ημερομηνία περιόδου: {multi_period.index[0].date()}")
print(first_row.round(2))
print(f"\nΤελευταία ημερομηνία περιόδου: {multi_period.index[-1].date()}")
print(last_row.round(2))

total_ret = (last_row / first_row - 1) * 100
print("\nΣυνολικές αποδόσεις (%) ανά τίτλο (με Adjusted Close):")
print(total_ret.round(2))

multi_close = pd.concat([data[t][f"{t}.Close"] for t in TICKERS], axis=1)
multi_close.columns = TICKERS
mc_period = multi_close.loc[START_INVEST:END_INVEST]
price_ret = (mc_period.iloc[-1] / mc_period.iloc[0] - 1) * 100
print("\nΑποδόσεις τιμής (%) - μόνο για σύγκριση, με μη προσαρμοσμένη Close:")
print(price_ret.round(2))

# ========================================================================
# 3) ΔΕΙΚΤΗΣ SMA CROSSOVER - πρώτη μετοχή (AMD), 1 έτος (Έτος 3)
# ========================================================================
print("\n" + "=" * 70)
print("3) ΔΕΙΚΤΗΣ SMA CROSSOVER - AMD, Έτος 3")
print("=" * 70)

amd_close = data["AMD"]["AMD.Close"]
sma50 = amd_close.rolling(window=50).mean()
sma200 = amd_close.rolling(window=200).mean()
sma = pd.DataFrame({"Close": amd_close, "sma50": sma50, "sma200": sma200})
sma_y3 = sma.loc[START_YEAR3:END_INVEST].dropna()

diff = sma_y3["sma50"] - sma_y3["sma200"]
sign = np.sign(diff)
cross = sign.diff()
bullish_dates = sma_y3.index[cross > 0]
bearish_dates = sma_y3.index[cross < 0]

print("Σήματα ΑΓΟΡΑΣ (bullish crossover):", [d.date().strftime('%d/%m/%Y') for d in bullish_dates])
print("Σήματα ΠΩΛΗΣΗΣ (bearish crossover):", [d.date().strftime('%d/%m/%Y') for d in bearish_dates])

# ========================================================================
# 4) ΔΕΙΚΤΗΣ RSI - πρώτη μετοχή (AMD), 1 έτος (Έτος 3), μέθοδος Wilder
# ========================================================================
print("\n" + "=" * 70)
print("4) ΔΕΙΚΤΗΣ RSI (14 ημερών, Wilder) - AMD, Έτος 3")
print("=" * 70)

close = data["AMD"]["AMD.Close"]
delta = close.diff().dropna()
up = delta.clip(lower=0)
down = -delta.clip(upper=0)

up_avg = up.copy() * np.nan
down_avg = down.copy() * np.nan

up_avg.iloc[13] = up.iloc[0:14].mean()
down_avg.iloc[13] = down.iloc[0:14].mean()

for i in range(14, len(up)):
    up_avg.iloc[i] = (up_avg.iloc[i - 1] * 13 + up.iloc[i]) / 14
    down_avg.iloc[i] = (down_avg.iloc[i - 1] * 13 + down.iloc[i]) / 14

rs = up_avg / down_avg
rsi = 100 - 100 / (1 + rs)
rsi_y3 = rsi.loc[START_YEAR3:END_INVEST].dropna()

rvec = rsi_y3.values
dvec = rsi_y3.index
sell_signals = [dvec[i + 1] for i in range(len(rvec) - 1) if rvec[i] > 70 and rvec[i + 1] <= 70]
buy_signals = [dvec[i + 1] for i in range(len(rvec) - 1) if rvec[i] < 30 and rvec[i + 1] >= 30]

print("Σήματα ΠΩΛΗΣΗΣ (RSI διασχίζει κάτω από 70):", [d.date().strftime('%d/%m/%Y') for d in sell_signals])
print("Σήματα ΑΓΟΡΑΣ (RSI διασχίζει πάνω από 30):", [d.date().strftime('%d/%m/%Y') for d in buy_signals])
print(f"Εύρος RSI στην περίοδο: {rsi_y3.min():.2f} - {rsi_y3.max():.2f}")

# ========================================================================
# 5) ΑΠΟΔΟΣΗ & ΚΙΝΔΥΝΟΣ ΧΑΡΤΟΦΥΛΑΚΙΟΥ (Έτος 2 - Έτος 3)
# ========================================================================
print("\n" + "=" * 70)
print("5) ΑΠΟΔΟΣΗ & ΚΙΝΔΥΝΟΣ ΧΑΡΤΟΦΥΛΑΚΙΟΥ")
print("=" * 70)

total_investment = sum(INVESTMENT.values())
weights = {t: INVESTMENT[t] / total_investment for t in TICKERS}
print("Βάρη χαρτοφυλακίου:", {t: round(w * 100, 2) for t, w in weights.items()}, "%")
print("Συνολικό αρχικό κεφάλαιο:", total_investment)

port_return_pct = sum(weights[t] * total_ret[t] for t in TICKERS)
final_value = total_investment * (1 + port_return_pct / 100)
print(f"\nΣυνολική απόδοση χαρτοφυλακίου: {port_return_pct:.2f}%")
print(f"Τελική αξία χαρτοφυλακίου: {final_value:.2f}")

daily_ret = multi_period.pct_change().dropna()
cov_daily = daily_ret.cov()
cov_annual = cov_daily * 252
w_vec = np.array([weights[t] for t in TICKERS])
port_variance = w_vec @ cov_annual.values @ w_vec
port_stdev = np.sqrt(port_variance)

print("\nΕτησιοποιημένος πίνακας συνδιακύμανσης:")
print(cov_annual.round(4))
print(f"\nΔιακύμανση χαρτοφυλακίου (ετησιοποιημένη): {port_variance:.4f}")
print(f"Τυπική απόκλιση χαρτοφυλακίου (ετησιοποιημένη): {port_stdev * 100:.2f}%")

# ========================================================================
# 6) ΒΕΛΤΙΣΤΟΠΟΙΗΣΗ ΧΑΡΤΟΦΥΛΑΚΙΟΥ MARKOWITZ (μηνιαία δεδομένα, βήμα 1%)
# ========================================================================
print("\n" + "=" * 70)
print("6) ΒΕΛΤΙΣΤΟΠΟΙΗΣΗ ΧΑΡΤΟΦΥΛΑΚΙΟΥ (Markowitz)")
print("=" * 70)

monthly_ret = {}
for t in TICKERS:
    s = data[t][f"{t}.Adjusted"].loc[START_INVEST:END_INVEST]
    m = s.resample('ME').last()
    r = m.pct_change().dropna()
    monthly_ret[t] = r
monthly_df = pd.DataFrame(monthly_ret)
print(f"Μηνιαίες αποδόσεις ({len(monthly_df)} παρατηρήσεις):")
print(monthly_df.round(4))

mean_m = monthly_df.mean()
cov_m = monthly_df.cov()

grid = np.round(np.arange(0, 1.001, 0.01), 2)
records = []
for w1 in grid:
    for w2 in grid:
        if w1 + w2 > 1:
            continue
        for w3 in grid:
            w4 = round(1 - w1 - w2 - w3, 2)
            if w4 < 0 or w4 > 1:
                continue
            wv = np.array([w1, w2, w3, w4])
            port_ret = wv @ mean_m.values
            port_risk = np.sqrt(wv @ cov_m.values @ wv)
            records.append((w1, w2, w3, w4, port_ret, port_risk))

results = pd.DataFrame(records, columns=["w.AMD", "w.AXON", "w.GME", "w.PFE", "PortRet", "PortRisk"])
Rf = 0
results["Sharpe"] = (results["PortRet"] - Rf) / results["PortRisk"]

minvar = results.loc[results["PortRisk"].idxmin()]
tangency = results.loc[results["Sharpe"].idxmax()]

print("\nα) Χαρτοφυλάκιο Ελάχιστου Κινδύνου (Minimum Variance):")
print(minvar.round(4))
print(f"   Ετησιοποιημένη απόδοση: {minvar['PortRet']*12*100:.2f}%   "
      f"Ετησιοποιημένος κίνδυνος: {minvar['PortRisk']*np.sqrt(12)*100:.2f}%")

print("\nβ) Χαρτοφυλάκιο Μεγίστου Δείκτη Sharpe (Tangency):")
print(tangency.round(4))
print(f"   Ετησιοποιημένη απόδοση: {tangency['PortRet']*12*100:.2f}%   "
      f"Ετησιοποιημένος κίνδυνος: {tangency['PortRisk']*np.sqrt(12)*100:.2f}%")
