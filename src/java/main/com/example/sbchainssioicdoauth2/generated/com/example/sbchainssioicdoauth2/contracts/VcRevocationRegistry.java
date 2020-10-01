package com.example.sbchainssioicdoauth2.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.11.
 */
@SuppressWarnings("rawtypes")
public class VcRevocationRegistry extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506101c7806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80634294857f1461003b578063b75c7dc61461006c575b600080fd5b6100586004803603602081101561005157600080fd5b503561008b565b604080519115158252519081900360200190f35b6100896004803603602081101561008257600080fd5b50356100db565b005b6000805461009b575060006100d6565b600082815260016020526040812054815481106100b457fe5b906000526020600020906002020160010160019054906101000a900460ff1690505b919050565b604080516060810182528281526000602080830182815260018486018181528454808301865585805295517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56360029097029687015591517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5649095018054925160ff199093169515159590951761ff00191661010092151592909202919091179093558154948252919091522060001991909101905556fea265627a7a723158206523d7ba2d20a90981250a0669ce0246d3ff100bb745d2c880debb9abb715e7264736f6c63430005100032";

    public static final String FUNC_ISREVOKED = "isRevoked";

    public static final String FUNC_REVOKE = "revoke";

    @Deprecated
    protected VcRevocationRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VcRevocationRegistry(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VcRevocationRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VcRevocationRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> isRevoked(byte[] _uuid) {
        final Function function = new Function(FUNC_ISREVOKED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_uuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> revoke(byte[] _uuid) {
        final Function function = new Function(
                FUNC_REVOKE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_uuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static VcRevocationRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VcRevocationRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VcRevocationRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VcRevocationRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VcRevocationRegistry load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VcRevocationRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VcRevocationRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VcRevocationRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VcRevocationRegistry> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VcRevocationRegistry.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<VcRevocationRegistry> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VcRevocationRegistry.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VcRevocationRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VcRevocationRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VcRevocationRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VcRevocationRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
