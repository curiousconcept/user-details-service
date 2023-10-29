package com.aalechnovic.userdetailservice.crypto.persistence;


import jakarta.persistence.*;

@Entity
@Table(name = "encrypted_user_details")
public class EncryptedUserDetails {

    @Id
    @Column(name = "encrypted_user_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "random_crypt_bytes")
    private byte[] randomCryptBytes;
    @Lob
    @Column(name = "user_details")
    private byte[] userDetails;

    private EncryptedUserDetails() {
    }

    public EncryptedUserDetails(Long id, byte[] randomCryptBytes, byte[] userDetails) {
        this.id = id;
        this.randomCryptBytes = randomCryptBytes;
        this.userDetails = userDetails;
    }

    public Long getId() {
        return id;
    }

    public byte[] getRandomCryptBytes() {
        return randomCryptBytes;
    }

    public byte[] getUserDetails() {
        return userDetails;
    }
}
