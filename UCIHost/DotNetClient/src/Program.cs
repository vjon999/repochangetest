using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace UCIClient
{
    class UCICLientProgram
    {
        private IPEndPoint endPointAddress;
        private Socket sendingSocket;
        bool done = false;
        const int BUFFER_SIZE = 1024;
        
        private EndPoint ep;
        private byte[] receiveBuffer = new byte[BUFFER_SIZE];
        private byte[] sendBuffer;
        private static Dictionary<String, String> properties = new Dictionary<string, string>();

        static UCICLientProgram()
        {
            foreach (var row in File.ReadAllLines("clientConfig.ini"))
                properties.Add(row.Split('=')[0], string.Join("=", row.Split('=').Skip(1).ToArray()));
        }

        public UCICLientProgram(String ip, int port)
        {
            endPointAddress = new IPEndPoint(IPAddress.Parse(ip), port);
            sendingSocket = new Socket(endPointAddress.Address.AddressFamily, SocketType.Dgram, ProtocolType.Udp);
            sendingSocket.Connect(endPointAddress);
            ep = (EndPoint)endPointAddress;
        }

        public void init()
        {
            //String msg = "get_available_engines";
            //sendBuffer = Encoding.ASCII.GetBytes(msg);
            //sendingSocket.Send(sendBuffer, 0, sendBuffer.Length, SocketFlags.None);
            sendingSocket.BeginReceiveFrom(receiveBuffer, 0, receiveBuffer.Length, 0, ref ep, new AsyncCallback(ReceiveCallback), null);

            while (!done)
            {
                string text_to_send = Console.ReadLine();
                
                    try
                    {
                        sendBuffer = Encoding.ASCII.GetBytes(text_to_send + "\n");
                        sendingSocket.Send(sendBuffer, 0, sendBuffer.Length, SocketFlags.None);
                    }
                    catch (Exception send_exception)
                    {
                        Console.WriteLine(" Exception {0}", send_exception.Message);
                    }
                
            } // end of while (!done)
        }

        String receivedMessage = null;
        public void ReceiveCallback(IAsyncResult ar)
        {
                // Receive all data
                int dataLen = this.sendingSocket.EndReceive(ar);
                receivedMessage = Encoding.ASCII.GetString(receiveBuffer, 0, dataLen);
                if ("exit".Equals(receivedMessage))
                {
                    done = true;
                    Environment.Exit(0);
                }
                else
                {
                    Console.Write(receivedMessage);
                    receiveBuffer = new byte[BUFFER_SIZE];
                    sendingSocket.BeginReceiveFrom(receiveBuffer, 0, receiveBuffer.Length, SocketFlags.None, ref ep, new AsyncCallback(this.ReceiveCallback), null);   
                }
        }

        static void Main(string[] args)
        {
            UCICLientProgram program = new UCICLientProgram(properties["ip"], Int32.Parse(properties["port"]));
            program.init();
        }
    }
}
