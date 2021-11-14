<?php

namespace App\Database\Seeds;

use App\Models\Rol;
use CodeIgniter\Database\Seeder;
use Faker\Factory;

class PersonaSeeder extends Seeder
{

    public function run()
    {
        helper('secure_password');
        for ($i = 0; $i < 10; $i++) { //to add 10 clients. Change limit as desired
            $this->db->table('personas')->insert($this->generatePersonas());
        }
    }

    private function generatePersonas(): array
    {
        $faker = Factory::create();
        $rol = new Rol();
        $dataRol = $rol->asObject()->orderBy('RAND()')->first();
        return [
            'correo' => $faker->email,
            'password' => hashPassword('123123'),
            'nombres' => $faker->name(),
            'estado' => random_int(0,1),
            'idJefe' => 0,
            'rol_id' => $dataRol->id
        ];
    }
}