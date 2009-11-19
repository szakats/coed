package coed.collab.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

public class CoedConnectionAcceptor extends IoHandlerAdapter {
	
	private IoAcceptor acceptor = new SocketAcceptor();
	private IoAcceptorConfig config = new SocketAcceptorConfig();
	
	public interface Listener {
		public void connected(CoedConnection conn);
	}
	
	private LinkedList<Listener> listeners = new LinkedList<Listener>();
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	private CoedConnection getConnFromIo(IoSession io, Object details) {
		Object attr = io.getAttribute("c");
		if(!(attr instanceof CoedConnection)) {
			System.out.println("found session " + io +
					" having an invalid 'c' attribute " + attr +
					" details: " + details);
			return null;
		}
		
		return (CoedConnection)attr;
	}
	
	@Override
    public void sessionCreated(IoSession session) {
		CoedConnection conn = CoedConnection.bind(session);
    	session.setAttribute("c", conn);
    	for(Listener listener : listeners)
    		listener.connected(conn);
    }
	
	@Override
	public void messageReceived(IoSession session, Object message) {
		CoedConnection conn = getConnFromIo(session, message);
		if(conn != null)
			conn.messageReceived(session, message);
	}
	
	@Override
	public void sessionClosed(IoSession session) {
		CoedConnection conn = getConnFromIo(session, "");
		if(conn != null)
			conn.sessionClosed(session);
	}
	
	public CoedConnectionAcceptor() {
		
	}
	
	public void listen(int port) {
	    DefaultIoFilterChainBuilder chain = config.getFilterChain();
	    
        chain.addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
        
        try {
        	acceptor.bind(new InetSocketAddress(port), this, 
                config);
        } catch(IOException e) {
        	e.printStackTrace();
        }
	}
}
