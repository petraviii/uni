####################################################################
# ΧΡΗΜΑΤΟΟΙΚΟΝΟΜΙΚΑ ΥΠΟΛΟΓΙΣΤΙΚΑ ΠΑΚΕΤΑ
# Ανάλυση Χαρτοφυλακίου Μετοχών: AMD, AXON, GME, PFE
# Αρχικό κεφάλαιο: 4.000 AMD, 3.000 AXON, 2.000 GME, 2.000 PFE
####################################################################

library(base)
library(zoo)
library(xts)
library(quantmod)

setwd("data")   # φάκελος με τα csv αρχεία

####################################################################
# ΣΥΝΑΡΤΗΣΗ ΕΙΣΑΓΩΓΗΣ ΔΕΔΟΜΕΝΩΝ
####################################################################
import_stock <- function(file, ticker) {
  data <- read.csv(file, header = TRUE)
  date <- as.Date(data$Date, format = "%Y-%m-%d")
  data <- cbind(date, data[,-1])
  data <- data[order(data$date),]
  data <- xts(data[,2:7], order.by = data[,1])
  names(data) <- paste(c(paste0(ticker,".Open"), paste0(ticker,".High"),
                          paste0(ticker,".Low"), paste0(ticker,".Close"),
                          paste0(ticker,".Adjusted"), paste0(ticker,".Volume")))
  return(data)
}

data.AMD  <- import_stock("AMD.csv",  "AMD")
data.AXON <- import_stock("AXON.csv", "AXON")
data.GME  <- import_stock("GME.csv",  "GME")
data.PFE  <- import_stock("PFE.csv",  "PFE")

####################################################################
# 1) ΕΛΕΓΧΟΣ ΔΕΔΟΜΕΝΩΝ (3 τρόποι)
####################################################################

# Τρόπος 1: Οπτικός έλεγχος - γράφημα τιμής κλεισίματος
plot(data.AMD$AMD.Close,   main = "AMD - Τιμή Κλεισίματος (έλεγχος δεδομένων)")
plot(data.AXON$AXON.Close, main = "AXON - Τιμή Κλεισίματος (έλεγχος δεδομένων)")
plot(data.GME$GME.Close,   main = "GME - Τιμή Κλεισίματος (έλεγχος δεδομένων)")
plot(data.PFE$PFE.Close,   main = "PFE - Τιμή Κλεισίματος (έλεγχος δεδομένων)")

# Τρόπος 2: Έλεγχος διάστασης (dim) - αναμενόμενες ~754 παρατηρήσεις (3 έτη δεδομένων)
dim(data.AMD)
dim(data.AXON)
dim(data.GME)
dim(data.PFE)

# Τρόπος 3: Συνοπτικά στατιστικά (summary) - έλεγχος ακραίων/αρνητικών τιμών & ελλειπουσών (NA)
summary(data.AMD)
summary(data.AXON)
summary(data.GME)
summary(data.PFE)

####################################################################
# ΟΡΙΣΜΟΣ ΠΕΡΙΟΔΩΝ
# Δεδομένα: 31/12/2010 - 30/12/2013 (3 έτη ημερήσιων δεδομένων)
#   Έτος 1: 31/12/2010 - 30/12/2011
#   Έτος 2: 03/01/2012 - 31/12/2012
#   Έτος 3: 02/01/2013 - 30/12/2013
# Επένδυση: από Έτος 2 έως Έτος 3 -> 03/01/2012 έως 30/12/2013
####################################################################
start.invest <- "2012-01-03"
end.invest   <- "2013-12-30"
start.year3  <- "2013-01-02"   # 1 έτος (εντός της επένδυσης) για SMA & RSI

####################################################################
# 2) ΣΥΝΟΛΙΚΕΣ ΑΠΟΔΟΣΕΙΣ (Έτος 2 έως Έτος 3)
####################################################################

multi <- data.AMD[,5]
multi <- merge(multi, data.AXON[,5])
multi <- merge(multi, data.GME[,5])
multi <- merge(multi, data.PFE[,5])
names(multi) <- c("AMD","AXON","GME","PFE")

multi.period <- subset(multi, index(multi) >= start.invest & index(multi) <= end.invest)

period.ret <- multi.period[c(1, nrow(multi.period)),]
period.ret

rets <- lapply(period.ret, Delt)
rets <- data.frame(rets)
rets <- rets[2,]*100
names(rets) <- c("AMD","AXON","GME","PFE")
rets    # Συνολικές αποδόσεις (%) ανά τίτλο

####################################################################
# 3) ΔΕΙΚΤΗΣ SMA CROSSOVER - AMD, 1 έτος (Έτος 3)
####################################################################
AMD.sma <- data.AMD[,4]
AMD.sma$sma50  <- rollmeanr(AMD.sma$AMD.Close, k = 50)
AMD.sma$sma200 <- rollmeanr(AMD.sma$AMD.Close, k = 200)

AMD.sma.y3 <- subset(AMD.sma, index(AMD.sma) >= start.year3 & index(AMD.sma) <= end.invest)

y.range <- range(AMD.sma.y3, na.rm = TRUE)
plot(x = index(AMD.sma.y3), xlab = "Date", y = AMD.sma.y3$AMD.Close, ylim = y.range,
     ylab = "Price ($)", type = "l",
     main = "AMD - Simple Moving Average Crossover\n02/01/2013 - 30/12/2013")
lines(x = index(AMD.sma.y3), y = AMD.sma.y3$sma50)
lines(x = index(AMD.sma.y3), y = AMD.sma.y3$sma200, lty = 2)
legend("topleft", c("AMD Price","50-Day SMA","200-Day SMA"), lty = c(1,1,2))

AMD.sma.y3$diff <- AMD.sma.y3$sma50 - AMD.sma.y3$sma200
AMD.sma.y3 <- AMD.sma.y3[!is.na(AMD.sma.y3$diff),]
sign.diff <- sign(AMD.sma.y3$diff)
cross <- diff(sign.diff)
bullish.dates <- index(AMD.sma.y3)[which(cross > 0) + 1]   # Bullish crossover -> BUY
bearish.dates <- index(AMD.sma.y3)[which(cross < 0) + 1]   # Bearish crossover -> SELL
bullish.dates
bearish.dates

####################################################################
# 4) ΔΕΙΚΤΗΣ RSI - AMD, 1 έτος (Έτος 3)
####################################################################
AMD.RSI <- data.AMD[,4]
AMD.RSI$delta <- diff(AMD.RSI$AMD.Close)

AMD.RSI$up   <- ifelse(AMD.RSI$delta > 0, 1, 0)
AMD.RSI$down <- ifelse(AMD.RSI$delta < 0, 1, 0)

AMD.RSI$up.val   <- AMD.RSI$delta * AMD.RSI$up
AMD.RSI$down.val <- -AMD.RSI$delta * AMD.RSI$down
AMD.RSI <- AMD.RSI[-1,]

AMD.RSI$up.first.avg   <- rollapply(AMD.RSI$up.val,   width = 14, FUN = mean, fill = NA, na.rm = TRUE)
AMD.RSI$down.first.avg <- rollapply(AMD.RSI$down.val, width = 14, FUN = mean, fill = NA, na.rm = TRUE)

up.val   <- as.numeric(AMD.RSI$up.val)
down.val <- as.numeric(AMD.RSI$down.val)
AMD.RSI$up.avg   <- AMD.RSI$up.first.avg
AMD.RSI$down.avg <- AMD.RSI$down.first.avg
for (i in 15:nrow(AMD.RSI)) {
  AMD.RSI$up.avg[i]   <- ((AMD.RSI$up.avg[i-1]*13   + up.val[i])   / 14)
  AMD.RSI$down.avg[i] <- ((AMD.RSI$down.avg[i-1]*13 + down.val[i]) / 14)
}

AMD.RSI$RS  <- AMD.RSI$up.avg / AMD.RSI$down.avg
AMD.RSI$RSI <- 100 - (100 / (1 + AMD.RSI$RS))

AMD.RSI.y3 <- subset(AMD.RSI[,ncol(AMD.RSI)], index(AMD.RSI) >= start.year3 & index(AMD.RSI) <= end.invest)

title1 <- "AMD - Relative Strength Index"
title2 <- "02/01/2013 - 30/12/2013"
plot(x = index(AMD.RSI.y3), xlab = "Date", y = AMD.RSI.y3$RSI,
     ylab = "RSI (14-Day)", ylim = c(0,100), type = "l", main = paste(title1,"\n",title2))
abline(h = c(30,70), lty = 2)

rsi.vec <- as.numeric(AMD.RSI.y3$RSI)
dates.vec <- index(AMD.RSI.y3)
sell.signals <- dates.vec[which(rsi.vec[-length(rsi.vec)] > 70 & rsi.vec[-1] <= 70) + 1]
buy.signals  <- dates.vec[which(rsi.vec[-length(rsi.vec)] < 30 & rsi.vec[-1] >= 30) + 1]
sell.signals
buy.signals

####################################################################
# 5) ΑΠΟΔΟΣΗ & ΚΙΝΔΥΝΟΣ ΧΑΡΤΟΦΥΛΑΚΙΟΥ (Έτος 2-Έτος 3)
####################################################################
i.AMD <- 4000; i.AXON <- 3000; i.GME <- 2000; i.PFE <- 2000
total.invest <- i.AMD + i.AXON + i.GME + i.PFE

w.AMD  <- i.AMD  / total.invest
w.AXON <- i.AXON / total.invest
w.GME  <- i.GME  / total.invest
w.PFE  <- i.PFE  / total.invest

wgt <- c(w.AMD, w.AXON, w.GME, w.PFE)
mat.wgt <- matrix(wgt, 1)
ret.vec <- c(rets$AMD, rets$AXON, rets$GME, rets$PFE)
mat.ret <- matrix(ret.vec, 4)
port.ret <- mat.wgt %*% mat.ret
port.ret                                    # Συνολική απόδοση χαρτοφυλακίου (%)
final.value <- total.invest * (1 + port.ret/100)
final.value                                 # Τελική αξία χαρτοφυλακίου

mat.price <- matrix(multi.period, nrow(multi.period))
prc2ret <- function(x) Delt(x)
mat.rets.d <- apply(mat.price, 2, function(x){prc2ret(c(x))})
mat.rets.d <- mat.rets.d[-1,]
colnames(mat.rets.d) <- c("AMD","AXON","GME","PFE")

VCOV <- cov(mat.rets.d)
VCOV.annual <- 252 * VCOV
VCOV.annual

tmat.wgt <- t(mat.wgt)
port.var <- mat.wgt %*% VCOV.annual %*% tmat.wgt
port.var[1,1]
port.sd <- sqrt(port.var)
port.sd[1,1]                                # Ετησιοποιημένος κίνδυνος χαρτοφυλακίου

####################################################################
# 6) ΒΕΛΤΙΣΤΟΠΟΙΗΣΗ ΧΑΡΤΟΦΥΛΑΚΙΟΥ MARKOWITZ - Μηνιαία δεδομένα, βήμα 1%
####################################################################

to_monthly_ret <- function(price.xts) {
  m <- to.monthly(price.xts)
  m <- m[,4]
  r <- Delt(m)
  return(r[-1,])
}

AMD.m  <- subset(data.AMD[,5],  index(data.AMD)  >= start.invest & index(data.AMD)  <= end.invest)
AXON.m <- subset(data.AXON[,5], index(data.AXON) >= start.invest & index(data.AXON) <= end.invest)
GME.m  <- subset(data.GME[,5],  index(data.GME)  >= start.invest & index(data.GME)  <= end.invest)
PFE.m  <- subset(data.PFE[,5],  index(data.PFE)  >= start.invest & index(data.PFE)  <= end.invest)

AMD.mret  <- to_monthly_ret(AMD.m)
AXON.mret <- to_monthly_ret(AXON.m)
GME.mret  <- to_monthly_ret(GME.m)
PFE.mret  <- to_monthly_ret(PFE.m)

monthly.ret <- cbind(AMD.mret, AXON.mret, GME.mret, PFE.mret)
names(monthly.ret) <- c("AMD","AXON","GME","PFE")
monthly.ret

mean.m <- colMeans(monthly.ret)
cov.m  <- cov(monthly.ret)

grid <- seq(0, 1, by = 0.01)
combos <- expand.grid(w1 = grid, w2 = grid, w3 = grid)
combos$w4 <- round(1 - combos$w1 - combos$w2 - combos$w3, 2)
combos <- subset(combos, w4 >= 0 & w4 <= 1 & round(w1+w2+w3+w4,2) == 1)

port.ret.vec  <- numeric(nrow(combos))
port.risk.vec <- numeric(nrow(combos))
for (i in 1:nrow(combos)) {
  w <- as.numeric(combos[i,])
  port.ret.vec[i]  <- sum(w * mean.m)
  port.risk.vec[i] <- sqrt(w %*% cov.m %*% w)
}
combos$PortRet  <- port.ret.vec
combos$PortRisk <- port.risk.vec

Rf <- 0
combos$Sharpe <- (combos$PortRet - Rf) / combos$PortRisk

minvar.port <- subset(combos, PortRisk == min(combos$PortRisk))
minvar.port

tangency.port <- subset(combos, Sharpe == max(combos$Sharpe))
tangency.port

minvar.port$AnnRet  <- minvar.port$PortRet * 12
minvar.port$AnnRisk <- minvar.port$PortRisk * sqrt(12)
tangency.port$AnnRet  <- tangency.port$PortRet * 12
tangency.port$AnnRisk <- tangency.port$PortRisk * sqrt(12)
minvar.port
tangency.port
