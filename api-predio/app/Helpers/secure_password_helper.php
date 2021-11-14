<?php

/**
 * Encripta la contraseña a modo Hash
 */
function hashPassword($plainPassword)
{
    return password_hash($plainPassword, PASSWORD_BCRYPT);
}
/**
 * Verifica que la contraseña encriptada sea válido
 */
function verifyPassword($plainPassword, $hash): bool
{
   return  password_verify($plainPassword, $hash);
}
