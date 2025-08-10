package com.zinios.onboard.Repository;

import com.zinios.onboard.Entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
}
