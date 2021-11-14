<?php

namespace App\Database\Seeds;

use App\Models\Predio;
use App\Models\Rol;
use CodeIgniter\Database\Seeder;
use Faker\Factory;

class PredioSeeder extends Seeder
{

    public function run()
    {
        helper('secure_password');
        for ($i = 0; $i < 50; $i++) {
            $this->db->table('predios')->insert($this->generatePredios());
        }
    }

    private function generatePredios(): array
    {
        $faker = Factory::create();
        return [
            'direccion' => $faker->address,
            'nroMedidor' => $faker->bankAccountNumber,
            'contactoNombre' => $faker->firstName,
            'contactoApellido' => $faker->lastName,
            'dni' => $faker->numerify('########'),
            'ubigeo' => $faker->state . " - " . $faker->citySuffix,
            'latitud' => $faker->latitude(-4,-15),
            'longitud' => $faker->longitude(-76, -70),
        ];
    }
}