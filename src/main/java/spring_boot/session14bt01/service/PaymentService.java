package spring_boot.session14bt01.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import spring_boot.session14bt01.model.entity.Order;
import spring_boot.session14bt01.model.entity.Wallet;

public class PaymentService {
    public void processPayment(Long orderId, Long walletId, double totalAmount) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            //Bắt đầu
            tx = session.beginTransaction();

            // Update order
            Order order = session.get(Order.class, orderId);
            order.setStatus("PAID");
            session.update(order);

            // Giả lập lỗi
            if (true){
                throw new RuntimeException("Lỗi thanh toán!");
            }

            // Update wallet
            Wallet wallet = session.get(Wallet.class, walletId);

            if (wallet.getBalance() < totalAmount) {
                throw new RuntimeException("Không đủ tiền!");
            }

            wallet.setBalance(wallet.getBalance() - totalAmount);
            session.update(wallet);

            //commit
            tx.commit();

        } catch (Exception e) {
            //Rollback
            if (tx != null){
                tx.rollback();
            }
            System.out.println("Rollback vì lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
