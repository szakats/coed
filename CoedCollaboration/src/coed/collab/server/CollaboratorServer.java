package coed.collab.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

public class CollaboratorServer {
	
    private IoAcceptor acceptor;
    private IoAcceptorConfig config;
    private DefaultIoFilterChainBuilder chain;
    
    private int port = 1234;
	
	public CollaboratorServer() {
		
	}
	
	void listen() {
	    acceptor = new SocketAcceptor();
	    config = new SocketAcceptorConfig();
	    chain = config.getFilterChain();
	    
        chain.addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
        
        try {
        	acceptor.bind(new InetSocketAddress(port), new ServerProtocolHandler(),
                config);
        } catch(IOException e) {
        	e.printStackTrace();
        }
	}
	
}
