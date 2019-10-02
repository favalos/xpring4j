package io.xpring.javascript;

import io.xpring.Utils;
import io.xpring.XpringKitException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.security.SecureRandom;

public class JavaScriptWalletFactory {
    public static String invalidMnemonicOrDerivationPathMessage = "Invalid mnemonic or derivation path.";

    private Value wallet;

    public JavaScriptWalletFactory() throws JavaScriptLoaderException {
        Context context = JavaScriptLoader.getContext();
        this.wallet = JavaScriptLoader.loadResource("Wallet", context);
    }

    public String getDefaultDerivationPath() throws JavaScriptLoaderException {
        Value getDefaultDerivationPathFunction = JavaScriptLoader.loadResource("getDefaultDerivationPath", wallet);
        return getDefaultDerivationPathFunction.execute().asString();
    }

    public JavaScriptWalletGenerationResult generateRandomWallet() throws JavaScriptLoaderException {
        byte [] randomBytes = randomBytes(16);
        String hexRandomBytes = Utils.byteArrayToHex(randomBytes);

        Value walletGenerationResult = this.wallet.invokeMember("generateRandomWallet", hexRandomBytes);
        return new JavaScriptWalletGenerationResult(
                walletGenerationResult.getMember("mnemonic").asString(),
                walletGenerationResult.getMember("derivationPath").asString(),
                new JavaScriptWallet(walletGenerationResult.getMember("wallet"))
        );
    }

    public JavaScriptWallet walletFromSeed(String seed) throws JavaScriptLoaderException, XpringKitException {
        Value wallet = this.wallet.invokeMember("generateWalletFromSeed", seed);
        if (wallet.isNull()) {
            throw new XpringKitException("Invalid Seed");
        }
        return new JavaScriptWallet(wallet);
    }

    public JavaScriptWallet walletFromMnemonicAndDerivationPath(String mnemonic, String derivationPath) throws JavaScriptLoaderException, XpringKitException {
        try {
            Value wallet;
            if (derivationPath != null) {
                wallet = this.wallet.invokeMember("generateWalletFromMnemonic", mnemonic, derivationPath);
            } else {
                wallet = this.wallet.invokeMember("generateWalletFromMnemonic", mnemonic);
            }

            if (wallet.isNull()) {
                throw new XpringKitException(invalidMnemonicOrDerivationPathMessage);
            }

            return new JavaScriptWallet(wallet);
        } catch (PolyglotException exception) {
            throw new XpringKitException(invalidMnemonicOrDerivationPathMessage);
        }
    }

    private byte [] randomBytes(int numBytes) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[numBytes];
        random.nextBytes(bytes);

        return bytes;
    }
}